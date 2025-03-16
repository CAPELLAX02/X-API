package com.x.backend.dto.auth.request;

import jakarta.validation.constraints.NotBlank;

public record SendVerificationEmailRequest(

        @NotBlank(message = "Username is required.")
        String username

) {
}
