package com.x.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SetPasswordRequest(

        @NotBlank(message = "Username is required.")
        String username,

        @NotBlank(message = "Password is required.")
        @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters.")
        String password

) {
}
