package com.x.backend.dto.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SendPasswordRecoveryEmailRequest(

        @NotBlank(message = "Email is required.")
        @Email(message = "Invalid email format.")
        @Size(max = 100, message = "Email can be at most 100 characters.")
        String email

) {
}
