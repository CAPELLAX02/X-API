package com.x.backend.services.auth.util;

import com.x.backend.dto.auth.response.AuthTokenResponse;
import com.x.backend.models.user.ApplicationUser;
import com.x.backend.models.user.auth.RefreshToken;
import com.x.backend.repositories.RefreshTokenRepository;
import com.x.backend.repositories.ValidAccessTokenRepository;
import com.x.backend.services.token.JwtService;

import java.time.Instant;

public class TokenIssuanceService {

    private static final long REFRESH_TOKEN_EXPIRATION_SECONDS = 604800; // 7 days

    public static AuthTokenResponse generateAndStoreTokens(ApplicationUser user,
                                                           JwtService jwtService,
                                                           ValidAccessTokenRepository validAccessTokenRepo,
                                                           RefreshTokenRepository refreshTokenRepo
    ) {
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        Long expiresIn = jwtService.getExpirationFromToken(accessToken);

        String jti = jwtService.extractJtiFromToken(accessToken);
        AccessTokenRegistry.store(jti, user, validAccessTokenRepo);

        refreshTokenRepo.findByUser(user).ifPresent(existing -> {
            existing.setUsed(true);
            refreshTokenRepo.save(existing);
        });

        RefreshToken newRefreshToken = new RefreshToken(
                refreshToken,
                user,
                Instant.now().plusSeconds(REFRESH_TOKEN_EXPIRATION_SECONDS)
        );
        refreshTokenRepo.save(newRefreshToken);

        return new AuthTokenResponse(accessToken, refreshToken, expiresIn);
    }


}
