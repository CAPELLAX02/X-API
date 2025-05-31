package com.x.backend.services.user.privacy;

import com.x.backend.dto.user.request.UpdatePrivacySettingsRequest;
import com.x.backend.dto.user.response.PrivacySettingsResponse;
import com.x.backend.exceptions.user.PrivacySettingsBaseNotFoundException;
import com.x.backend.utils.api.BaseApiResponse;

/**
 * Service interface responsible for retrieving and updating the privacy settings
 * of the currently authenticated user.
 */
public interface PrivacySettingsService {

    /**
     * Retrieves the privacy settings of the currently authenticated user.
     *
     * @param username the username of the authenticated user whose settings are being retrieved
     * @return the current privacy settings wrapped in {@link PrivacySettingsResponse}
     *
     * @throws PrivacySettingsBaseNotFoundException if no settings are found for the user
     */
    BaseApiResponse<PrivacySettingsResponse> getPrivacySettingsForCurrentUser(String username);

    /**
     * Updates the privacy settings of the currently authenticated user.
     *
     * @param username the username of the authenticated user whose settings are being updated
     * @param request the update request containing:
     *                - {@code messagePrivacy}: who can send direct messages
     *                - {@code mentionPrivacy}: who can mention the user in posts
     *                - {@code postVisibility}: who can view the user's posts
     * @return the updated privacy settings wrapped in {@link PrivacySettingsResponse}
     *
     * @throws PrivacySettingsBaseNotFoundException if no settings are found for the user
     */
    BaseApiResponse<PrivacySettingsResponse> updatePrivacySettingsForCurrentUser(String username, UpdatePrivacySettingsRequest request);

}
