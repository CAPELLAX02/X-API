package com.x.backend.services.media.image;

import com.x.backend.exceptions.image.FailedToUploadImageException;
import com.x.backend.models.media.Image;
import com.x.backend.models.media.enums.ImageType;
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
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public abstract class ImageUploadService {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    private static final List<String> SUPPORTED_EXTENSIONS = List.of(
            ".jpg", ".jpeg", ".png", ".gif", ".webp"
    );

    protected final ImageRepository imageRepository;

    @Value("${image.upload.dir}")
    private String baseImageUploadDir;

    public ImageUploadService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    protected Image storeImage(MultipartFile file, ImageType imageType) {
        try {
            validateFile(file);

            String extension = getFileExtension(file.getOriginalFilename());
            String uniqueFileName = UUID.randomUUID() + extension;

            Path directoryPath = Paths.get(baseImageUploadDir, imageType.name().toLowerCase());
            Files.createDirectories(directoryPath);

            Path fullPath = directoryPath.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), fullPath, StandardCopyOption.REPLACE_EXISTING);

            int width = 0, height = 0;
            try {
                BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
                if (bufferedImage != null) {
                    width = bufferedImage.getWidth();
                    height = bufferedImage.getHeight();
                }
            } catch (Exception ignored) {}

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
            throw new FailedToUploadImageException("Image upload failed. " + e.getMessage());
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FailedToUploadImageException("Uploaded file is empty.");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new FailedToUploadImageException("Image size exceeds 5MB limit.");
        }

        String extension = getFileExtension(file.getOriginalFilename()).toLowerCase();
        if (!SUPPORTED_EXTENSIONS.contains(extension)) {
            throw new FailedToUploadImageException(
                    "Unsupported file extension: " + extension + ". Supported extensions: " + SUPPORTED_EXTENSIONS
            );
        }
    }

    private String getFileExtension(String filename) {
        int dotIndex = Objects.requireNonNull(filename).lastIndexOf('.');
        return (dotIndex >= 0) ? filename.substring(dotIndex) : "";
    }

}
