package com.x.backend.services.auth.util;

import com.x.backend.dto.auth.request.StartRegistrationRequest;
import com.x.backend.exceptions.auth.EmailAlreadyInUseException;
import com.x.backend.exceptions.auth.UsernameAlreadyInUseException;
import com.x.backend.models.user.ApplicationUser;
import com.x.backend.models.user.PrivacySettings;
import com.x.backend.models.user.auth.Role;
import com.x.backend.models.user.auth.enums.PrivacyLevel;
import com.x.backend.models.user.auth.enums.RoleType;
import com.x.backend.repositories.ApplicationUserRepository;
import com.x.backend.repositories.PrivacySettingsRepository;
import com.x.backend.repositories.RoleRepository;

import java.util.Collections;

public class UserRegistrationManager {

    public static void validateUniqueness(String email,
                                          String firstName,
                                          String lastName,
                                          ApplicationUserRepository applicationUserRepository,
                                          UsernameGenerationService usernameGenerationService
    ) {
        if (applicationUserRepository.existsByEmail(email)) {
            throw new EmailAlreadyInUseException(email);
        }

        String candidateUsername = usernameGenerationService.generateUniqueUsername(firstName, lastName);
        if (applicationUserRepository.existsByUsername(candidateUsername)) {
            throw new UsernameAlreadyInUseException();
        }
    }

    public static ApplicationUser createUser(StartRegistrationRequest startRegistrationRequest,
                                             ApplicationUserRepository applicationUserRepository,
                                             RoleRepository roleRepository,
                                             UsernameGenerationService usernameGenerationService
    ) {
        String username = usernameGenerationService.generateUniqueUsername(startRegistrationRequest.firstName(), startRegistrationRequest.lastName());

        ApplicationUser user = new ApplicationUser();
        user.setFirstName(startRegistrationRequest.firstName());
        user.setLastName(startRegistrationRequest.lastName());
        user.setEmail(startRegistrationRequest.email());
        user.setDateOfBirth(startRegistrationRequest.dateOfBirth());
        user.setUsername(username);
        user.setEnabled(false);

        Role role = roleRepository.findByAuthority(RoleType.ROLE_USER)
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setAuthority(RoleType.ROLE_USER);
                    return roleRepository.save(newRole);
                });

        user.setAuthorities(Collections.singleton(role));

        return user;
    }

    public static void initializePrivacySettings(ApplicationUser user, PrivacySettingsRepository privacySettingsRepository) {
        PrivacySettings privacySettings = new PrivacySettings();
        privacySettings.setUser(user);
        privacySettings.setMessagePrivacy(PrivacyLevel.EVERYONE);
        privacySettings.setMentionPrivacy(PrivacyLevel.EVERYONE);
        privacySettings.setPostVisibility(PrivacyLevel.EVERYONE);
        privacySettingsRepository.save(privacySettings);
    }

}
