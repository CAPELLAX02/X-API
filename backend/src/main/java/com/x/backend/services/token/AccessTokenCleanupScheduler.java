package com.x.backend.services.token;

import com.x.backend.repositories.ValidAccessTokenRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AccessTokenCleanupScheduler {

    private final ValidAccessTokenRepository validAccessTokenRepository;

    public AccessTokenCleanupScheduler(ValidAccessTokenRepository validAccessTokenRepository) {
        this.validAccessTokenRepository = validAccessTokenRepository;
    }

    @Scheduled(fixedRate = 3600000) // Cleans up in every 1 hour
    public void cleanExpiredAccessTokens() {
        validAccessTokenRepository.deleteAllByExpiresAtBefore(Instant.now());
    }

}
