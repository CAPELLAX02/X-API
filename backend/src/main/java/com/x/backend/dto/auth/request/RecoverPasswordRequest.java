package com.x.backend.dto.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RecoverPasswordRequest(

        @NotBlank(message = "Email is required.")
        @Email(message = "Invalid email format.")
        @Size(max = 100, message = "Email can be at most 100 characters.")
        String email,

        @NotBlank(message = "Recovery code is required.")
        String passwordRecoveryCode,

        @NotBlank(message = "New password is required.")
        @Size(min = 8, max = 64, message = "New password must be between 8 and 64 characters.")
        String newPassword,

        @NotBlank(message = "New password (again) is required.")
        @Size(min = 8, max = 64, message = "New password (again) must be between 8 and 64 characters.")
        String newPasswordAgain

) {
}
