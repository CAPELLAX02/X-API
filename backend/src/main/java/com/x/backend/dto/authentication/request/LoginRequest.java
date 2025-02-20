package com.x.backend.dto.authentication.request;

public record LoginRequest(
        String username,
        String password
) {
}
