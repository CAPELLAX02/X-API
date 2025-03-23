package com.x.backend.dto.user.request;

import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public record UpdateWebsiteRequest(

        @Size(max = 255, message = "Website URL can be at most 255 characters.")
        @URL
        String websiteUrl

) {
}
