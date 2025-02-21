package com.x.backend.dto;

import jakarta.validation.constraints.NotBlank;

public record StartEmailVerificationRequest(

        @NotBlank(message = "Username cannot be blank")
        String username


) {
}
