package com.x.backend.dto.user.request;

import jakarta.validation.constraints.Size;

public record UpdateBioRequest(

        @Size(max = 160, message = "Biography text can be at most 160 characters.")
        String bio

) {
}
