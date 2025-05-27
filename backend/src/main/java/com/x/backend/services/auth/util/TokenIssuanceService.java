package com.x.backend.services.auth.util;

import com.x.backend.dto.auth.response.AuthTokenResponse;
import com.x.backend.models.user.ApplicationUser;
import com.x.backend.models.user.auth.RefreshToken;
import com.x.backend.repositories.RefreshTokenRepository;
import com.x.backend.repositories.ValidAccessTokenRepository;
import com.x.backend.services.token.JwtService;

import java.time.Instant;

public class TokenIssuanceService {

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

        RefreshToken existing = refreshTokenRepo.findByUser(user).orElse(null);
        if (existing != null) {
            existing.setToken(refreshToken);
            existing.setExpiryDate(Instant.now().plusSeconds(604800));
            refreshTokenRepo.save(existing);
        }
        else {
            refreshTokenRepo.save(new RefreshToken(refreshToken, user, Instant.now().plusSeconds(604800)));
        }

        return new AuthTokenResponse(accessToken, refreshToken, expiresIn);
    }


}
