package com.x.backend.dto.authentication.request;

import com.x.backend.utils.validation.AgeLimit;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record RegisterUserRequest(

        @NotBlank(message = "First name cannot be blank")
        @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
        String firstName,

        @NotBlank(message = "Last name cannot be blank")
        @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
        String lastName,

        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Email must be valid")
        String email,

        @NotNull(message = "Date of birth cannot be null")
        @Past(message = "Date of birth must be in the past")
        @AgeLimit(min = 18, message = "User must be at least 18 years old")
        LocalDate dateOfBirth
) {
}
