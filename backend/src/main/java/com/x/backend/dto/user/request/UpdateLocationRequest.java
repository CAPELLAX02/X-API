package com.x.backend.dto.user.request;

import jakarta.validation.constraints.Size;

public record UpdateLocationRequest(

        @Size(max = 100, message = "Location field can be at most 160 characters.")
        String location

) {
}
