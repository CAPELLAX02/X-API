package com.x.backend.services.user.privacy;

import com.x.backend.dto.user.request.UpdatePrivacySettingsRequest;
import com.x.backend.dto.user.response.PrivacySettingsResponse;
import com.x.backend.utils.api.BaseApiResponse;

public interface PrivacySettingsService {

    BaseApiResponse<PrivacySettingsResponse> getPrivacySettingsForCurrentUser(String username);
    BaseApiResponse<PrivacySettingsResponse> updatePrivacySettingsForCurrentUser(String username, UpdatePrivacySettingsRequest request);

}
