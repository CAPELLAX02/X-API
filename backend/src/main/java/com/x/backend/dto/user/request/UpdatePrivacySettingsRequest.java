package com.x.backend.dto.user.request;

import com.x.backend.models.enums.PrivacyLevel;
import jakarta.validation.constraints.NotNull;

public record UpdatePrivacySettingsRequest(

        @NotNull(message = "Message privacy choice cannot be null.")
        PrivacyLevel messagePrivacy,

        @NotNull(message = "Mention privacy choice cannot be null.")
        PrivacyLevel mentionPrivacy,

        @NotNull(message = "Post visibility choice cannot be null.")
        PrivacyLevel postVisibility

) {
}
