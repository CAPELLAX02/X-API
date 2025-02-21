package com.x.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdatePhoneNumberRequest(

        @NotBlank(message = "Username cannot be blank")
        String username,

        @NotBlank(message = "Phone number cannot be blank")
        @Pattern(
                regexp = "^\\+?[1-9]\\d{1,14}$",
                message = "Invalid phone number format. Must be in E.164 format."
        )
        @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 digits")
        String phoneNumber
) {
}
