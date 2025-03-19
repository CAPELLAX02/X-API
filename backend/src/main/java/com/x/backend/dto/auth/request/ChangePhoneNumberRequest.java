package com.x.backend.dto.auth.request;

import jakarta.validation.constraints.NotBlank;

public record ChangePhoneNumberRequest(

        @NotBlank(message = "New phone number is required.")
        String newPhoneNumber

) {
}
