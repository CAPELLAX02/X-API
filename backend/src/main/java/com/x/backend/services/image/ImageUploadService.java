package com.x.backend.services.image;

import com.x.backend.exceptions.image.FailedToUploadImageException;
import com.x.backend.models.entities.Image;
import com.x.backend.models.enums.ImageType;
import com.x.backend.repositories.ImageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public abstract class ImageUploadService {

    protected final ImageRepository imageRepository;

    @Value("${image.upload.dir}")
    private String baseImageUploadDir;

    public ImageUploadService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    protected Image storeImage(MultipartFile file, ImageType imageType) {
        try {
            validateImage(file);

            String extension = getFileExtension(Objects.requireNonNull(file.getOriginalFilename()));
            String generatedFileName = UUID.randomUUID() + extension;

            Path directoryPath = Paths.get(baseImageUploadDir, imageType.name().toLowerCase());
            Files.createDirectories(directoryPath);

            Path fullPath = directoryPath.resolve(generatedFileName);
            Files.copy(file.getInputStream(), fullPath, StandardCopyOption.REPLACE_EXISTING);

            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();

            Image image = new Image();
            image.setImageUrl(fullPath.toString());
            image.setImageType(imageType);
            image.setSizeInKb(file.getSize() / 1024);
            image.setWidth(width);
            image.setHeight(height);
            image.setUploadedAt(LocalDateTime.now());

            return imageRepository.save(image);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new FailedToUploadImageException("Image upload failed.");
        }
    }

    private String getFileExtension(String fileName) {
        int index = fileName.lastIndexOf('.');
        return (index > 0) ? fileName.substring(index) : "";
    }

    private void validateImage(MultipartFile file) {
        long maxFileSize = 5 * 1024 * 1024;
        List<String> whitelistedExtensions = List.of("image/jpeg", "image/jpg", "image/png", "image/webp");
        if (file.isEmpty()) {
            throw new FailedToUploadImageException("Uploaded file is empty.");
        }
        if (!whitelistedExtensions.contains(file.getContentType())) {
            throw new FailedToUploadImageException("Unsupported image type: " + file.getContentType());
        }
        if (file.getSize() > maxFileSize) {
            throw new FailedToUploadImageException("Image size exceeds maximum allowed (5MB).");
        }
    }

}
