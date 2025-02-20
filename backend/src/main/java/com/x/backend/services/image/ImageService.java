package com.x.backend.services.image;

import com.x.backend.models.Image;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    Image uploadImage(MultipartFile file, String prefix);
    byte[] downloadImage(String imageName);
    String getImageType(String imageName);

}
