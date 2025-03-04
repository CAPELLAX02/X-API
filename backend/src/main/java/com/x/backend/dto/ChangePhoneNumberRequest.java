package com.x.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ChangePhoneNumberRequest(

        @NotBlank(message = "Old phone number is required.")
        @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 characters.")
        @Pattern(regexp = "^[0-9]+$", message = "Phone number must contain only numbers.")
        String oldPhoneNumber,

        @NotBlank(message = "New phone number is required.")
        @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 characters.")
        @Pattern(regexp = "^[0-9]+$", message = "Phone number must contain only numbers.")
        String newPhoneNumber

) {
}
