package com.x.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RecoverPasswordRequest(

        @NotBlank(message = "Recovery code is required.")
        String recoveryCode,

        @NotBlank(message = "New password is required.")
        @Size(min = 8, max = 64, message = "New password must be between 8 and 64 characters.")
        String newPassword

) {
}
