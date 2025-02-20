package com.x.backend.dto.authentication.request;

public record UpdatePasswordRequest(
        String username,
        String password
) {
}
