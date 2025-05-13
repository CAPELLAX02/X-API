package com.x.backend.dto.image.response;

import com.x.backend.models.image.enums.ImageType;

import java.time.LocalDateTime;

public record ImageResponse(

        Long id,
        String imageUrl,
        ImageType imageType,
        Long sizeInKb,
        int width,
        int height,
        LocalDateTime uploadedAt

) {
}
