package com.x.backend.dto.auth.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SetPhoneNumberRequest(

        @NotBlank(message = "Username is required.")
        String username,

        @NotBlank(message = "Phone number is required.")
        @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 characters.")
        String phoneNumber

) {
}
