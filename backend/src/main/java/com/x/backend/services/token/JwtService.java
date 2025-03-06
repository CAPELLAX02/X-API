package com.x.backend.services.token;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.function.Function;

public interface JwtService {

    String generateAccessToken(UserDetails userDetails);
    String generateAccessToken(Map<String, Object> claims, UserDetails userDetails);

    String generateRefreshToken(UserDetails userDetails);

    String extractUsernameFromToken(String token);
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    boolean isTokenValid(String token, UserDetails userDetails);
    boolean isTokenExpired(String token);

    long getExpirationFromToken(String token);

}
