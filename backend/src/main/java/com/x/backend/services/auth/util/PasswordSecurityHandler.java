package com.x.backend.services.auth.util;

import com.x.backend.exceptions.auth.PasswordReusedException;
import com.x.backend.models.user.ApplicationUser;
import com.x.backend.models.user.auth.PasswordHistory;
import com.x.backend.repositories.PasswordHistoryRepository;
import com.x.backend.security.password.PasswordEncodingConfig;

import java.time.Instant;
import java.util.List;

public class PasswordSecurityHandler {

    public static String encodePassword(String rawPassword, PasswordEncodingConfig passwordEncodingConfig) {
        return passwordEncodingConfig.passwordEncoder().encode(rawPassword);
    }

    public static void checkPasswordReuse(ApplicationUser user,
                                          String rawPassword,
                                          PasswordEncodingConfig passwordEncodingConfig,
                                          PasswordHistoryRepository passwordHistoryRepository
    ) {
        List<PasswordHistory> recentPasswords = passwordHistoryRepository.findTop3ByUserOrderByChangedAtDesc(user);
        for (PasswordHistory passwordHistory : recentPasswords) {
            if (passwordEncodingConfig.passwordEncoder().matches(rawPassword, passwordHistory.getPassword())) {
                throw new PasswordReusedException("Your password cannot be the same as your last 3 passwords.");
            }
        }
    }

    public static void storePasswordHistory(ApplicationUser user,
                                            String encodedPassword,
                                            PasswordHistoryRepository passwordHistoryRepository
    ) {
        PasswordHistory passwordHistory = new PasswordHistory(user, encodedPassword, Instant.now());
        passwordHistoryRepository.save(passwordHistory);
    }

}
