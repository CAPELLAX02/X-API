package com.x.backend.services.user.privacy;

import com.x.backend.dto.user.request.UpdatePrivacySettingsRequest;
import com.x.backend.dto.user.response.PrivacySettingsResponse;
import com.x.backend.exceptions.user.PrivacySettingsNotFoundException;
import com.x.backend.models.user.ApplicationUser;
import com.x.backend.models.user.PrivacySettings;
import com.x.backend.repositories.PrivacySettingsRepository;
import com.x.backend.services.user.UserService;
import com.x.backend.utils.api.BaseApiResponse;
import com.x.backend.utils.builder.PrivacySettingsResponseBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PrivacySettingsServiceImpl implements PrivacySettingsService {

    private final UserService userService;
    private final PrivacySettingsRepository privacySettingsRepository;
    private final PrivacySettingsResponseBuilder privacySettingsResponseBuilder;

    public PrivacySettingsServiceImpl(final UserService userService,
                                      final PrivacySettingsRepository privacySettingsRepository,
                                      final PrivacySettingsResponseBuilder privacySettingsResponseBuilder
    ) {
        this.userService = userService;
        this.privacySettingsRepository = privacySettingsRepository;
        this.privacySettingsResponseBuilder = privacySettingsResponseBuilder;
    }

    @Override
    public BaseApiResponse<PrivacySettingsResponse> getPrivacySettingsForCurrentUser(String username) {
        ApplicationUser user = userService.getUserByUsername(username);
        PrivacySettings privacySettings = privacySettingsRepository.findByUser(user).orElseThrow(PrivacySettingsNotFoundException::new);

        PrivacySettingsResponse res = privacySettingsResponseBuilder.buildPrivacySettingsResponse(privacySettings);
        return BaseApiResponse.success(res, "Privacy Settings of the current user retrieved.");
    }

    @Override
    public BaseApiResponse<PrivacySettingsResponse> updatePrivacySettingsForCurrentUser(String username, UpdatePrivacySettingsRequest req) {
        ApplicationUser user = userService.getUserByUsername(username);
        PrivacySettings privacySettings = privacySettingsRepository.findByUser(user).orElseThrow(PrivacySettingsNotFoundException::new);

        privacySettings.setMessagePrivacy(req.messagePrivacy());
        privacySettings.setMentionPrivacy(req.mentionPrivacy());
        privacySettings.setPostVisibility(req.postVisibility());
        privacySettingsRepository.save(privacySettings);

        PrivacySettingsResponse res = privacySettingsResponseBuilder.buildPrivacySettingsResponse(privacySettings);
        return BaseApiResponse.success(res, "Privacy Settings of the current user updated.");
    }

}
