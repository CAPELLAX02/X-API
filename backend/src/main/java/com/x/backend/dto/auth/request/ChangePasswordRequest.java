package com.x.backend.dto.auth.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(

        @NotBlank(message = "Old password is required.")
        String oldPassword,

        @NotBlank(message = "New password is required.")
        @Size(min = 8, max = 64, message = "Password must be between 8 and 64 character")
        String newPassword,

        @NotBlank(message = "New password (again) is required.")
        @Size(min = 8, max = 64, message = "Password must be between 8 and 64 character")
        String newPasswordAgain

) {
}
