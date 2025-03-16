package com.x.backend.dto.auth.request;

import com.x.backend.utils.validation.AgeLimit;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record StartRegistrationRequest(

        @NotBlank(message = "First name is required.")
        @Size(max = 50, message = "First name can be at most 50 characters.")
        String firstName,

        @NotBlank(message = "Last name is required.")
        @Size(max = 50, message = "Last name can be at most 50 characters.")
        String lastName,

        @NotBlank(message = "Email is required.")
        @Email(message = "Invalid email format.")
        @Size(max = 100, message = "Email can be at most 100 characters.")
        String email,

        @NotNull(message = "Date of birth is required.")
        @AgeLimit(min = 18, message = "You must be at least 18 years old to register.")
        LocalDate dateOfBirth
) {
}
