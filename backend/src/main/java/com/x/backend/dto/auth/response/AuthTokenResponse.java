package com.x.backend.dto.auth.response;

public record AuthTokenResponse(
        String accessToken,
        String refreshToken,
        Long expiresIn
) {
}
