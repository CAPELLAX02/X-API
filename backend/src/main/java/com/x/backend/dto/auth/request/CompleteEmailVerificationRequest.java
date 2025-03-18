package com.x.backend.dto.auth.request;

import jakarta.validation.constraints.NotBlank;

public record CompleteEmailVerificationRequest(

        @NotBlank(message = "Username is required.")
        String username,

        @NotBlank(message = "Verification code is required.")
        String verificationCode

) {
}
