package com.x.backend.services.token;

import com.x.backend.models.entities.ApplicationUser;
import io.jsonwebtoken.Claims;

import java.util.List;
import java.util.function.Function;

public interface JwtService {

    String generateAccessToken(ApplicationUser user);
    String generateRefreshToken(ApplicationUser user);

    String extractUsernameFromToken(String token);
    Long extractUserIdFromToken(String token);
    List<String> extractRoles(String token);
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    boolean isTokenValid(String token, ApplicationUser user);
    boolean isTokenExpired(String token);
    long getExpirationFromToken(String token);

}
