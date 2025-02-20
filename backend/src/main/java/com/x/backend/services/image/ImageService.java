package com.x.backend.services.image;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    String uploadImage(MultipartFile file, String prefix);
    byte[] downloadImage(String imageName);
    String getImageType(String imageName);

}
