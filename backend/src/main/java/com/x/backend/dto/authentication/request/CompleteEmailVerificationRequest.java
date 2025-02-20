package com.x.backend.dto.authentication.request;

public record CompleteEmailVerificationRequest(
        String username,
        String verificationCode
) {
}
