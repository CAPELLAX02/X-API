package com.x.backend.services.image;

import com.x.backend.models.media.Image;
import com.x.backend.models.media.enums.ImageType;
import com.x.backend.repositories.ImageRepository;
import com.x.backend.utils.builder.ImageResponseBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class PostImageService extends ImageUploadService {

    private final ImageResponseBuilder imageResponseBuilder;

    public PostImageService(final ImageRepository imageRepository,
                            final ImageResponseBuilder imageResponseBuilder
    ) {
        super(imageRepository);
        this.imageResponseBuilder = imageResponseBuilder;
    }

    public List<Image> uploadPostImages(List<MultipartFile> images) {
        return images.stream()
                .map(file -> storeImage(file, ImageType.POST_ATTACHMENT))
                .toList();
    }

}
