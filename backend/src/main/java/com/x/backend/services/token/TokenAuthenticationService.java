package com.x.backend.services.token;

import com.x.backend.models.user.ApplicationUser;
import com.x.backend.repositories.ValidAccessTokenRepository;
import com.x.backend.services.user.UserService;
import io.jsonwebtoken.JwtException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class TokenAuthenticationService {

    private final JwtService jwtService;
    private final UserService userService;
    private final ValidAccessTokenRepository validAccessTokenRepository;

    public TokenAuthenticationService(final JwtService jwtService,
                                      final UserService userService,
                                      final ValidAccessTokenRepository validAccessTokenRepository
    ) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.validAccessTokenRepository = validAccessTokenRepository;
    }

    public Authentication authenticateAccessToken(String token) {
        String tokenType = jwtService.extractClaim(token, claims -> claims.get("token_type", String.class));
        if (!"access".equals(tokenType)) {
            throw new JwtException("Invalid token type");
        }

        String jti = jwtService.extractJtiFromToken(token);
        if (!validAccessTokenRepository.existsByJti(jti)) {
            throw new JwtException("Token is revoked or expired");
        }

        String username = jwtService.extractUsernameFromToken(token);
        ApplicationUser user = userService.getUserByUsername(username);

        if (!jwtService.isTokenValid(token, user)) {
            throw new JwtException("Token validation failed");
        }

        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }

}
