package com.x.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SendVerificationEmailRequest(

        @NotBlank(message = "Username is required.")
        String username,

        @NotBlank(message = "Email is required.")
        @Email(message = "Invalid email format.")
        @Size(max = 100, message = "Email can be at most 100 characters.")
        String email

) {
}
