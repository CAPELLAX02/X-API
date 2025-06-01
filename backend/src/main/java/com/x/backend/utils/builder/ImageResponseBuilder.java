package com.x.backend.utils.builder;

import com.x.backend.dto.image.response.ImageResponse;
import com.x.backend.models.media.Image;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ImageResponseBuilder {

    public ImageResponse buildImageResponse(Image i) {
        return new ImageResponse(
                i.getId(),
                i.getImageUrl(),
                i.getImageType(),
                i.getSizeInKb(),
                i.getWidth(),
                i.getHeight(),
                i.getUploadedAt()
        );
    }

    public List<ImageResponse> buildImageResponses(List<Image> images) {
        List<ImageResponse> imageResponses = new ArrayList<>();
        for (Image i : images) {
            imageResponses.add(buildImageResponse(i));
        }
        return imageResponses;
    }

}
