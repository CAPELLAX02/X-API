package com.x.backend.utils.builder;

import com.x.backend.dto.user.response.PrivacySettingsResponse;
import com.x.backend.models.user.PrivacySettings;
import org.springframework.stereotype.Component;

@Component
public class PrivacySettingsResponseBuilder {

    public PrivacySettingsResponse buildPrivacySettingsResponse(PrivacySettings p) {
        return new PrivacySettingsResponse(
                p.getUser().getUsername(),
                p.getMessagePrivacy(),
                p.getMentionPrivacy(),
                p.getPostVisibility()
        );
    }

}
