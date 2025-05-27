package com.x.backend.services.auth.util;

import com.x.backend.models.user.ApplicationUser;
import com.x.backend.models.user.auth.ValidAccessToken;
import com.x.backend.repositories.ValidAccessTokenRepository;

import java.time.Instant;

public class AccessTokenRegistry {

    public static void revokeAll(ApplicationUser user, ValidAccessTokenRepository repo) {
        repo.deleteAllByUser(user);
    }

    public static void store(String jti, ApplicationUser user, ValidAccessTokenRepository repo) {
        Instant expiryTime = Instant.now().plusSeconds(900);
        repo.save(
                new ValidAccessToken(jti, user, expiryTime)
        );
    }

}
