package com.x.backend.dto;

public record AuthTokenResponse(
        String accessToken,
        String refreshToken,
        Long expiresIn
) {
}
