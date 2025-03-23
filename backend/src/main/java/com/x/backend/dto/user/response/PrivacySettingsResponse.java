package com.x.backend.dto.user.response;

import com.x.backend.models.enums.PrivacyLevel;

public record PrivacySettingsResponse(

        String username,
        PrivacyLevel messagePrivacy,
        PrivacyLevel mentionPrivacy,
        PrivacyLevel postVisibility

) {
}
