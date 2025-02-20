package com.x.backend.dto.authentication.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.core.type.filter.RegexPatternTypeFilter;

public record UpdatePasswordRequest(

        @NotBlank(message = "Username cannot be blank")
        String username,

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 8, message = "Password must be at least 8 characters")
        @Size(max = 64, message = "Password must be at maximum 64 characters")
        String password
) {
}
