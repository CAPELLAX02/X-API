package com.x.backend.dto.auth.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CompleteEmailVerificationRequest(

        @NotBlank(message = "Username is required.")
        String username,

        @NotBlank(message = "Verification code is required.")
        @Size(min = 6, max = 6, message = "Verification code must be 6 digits.")
        String verificationCode

) {
}
