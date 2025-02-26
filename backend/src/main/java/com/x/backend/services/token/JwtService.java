package com.x.backend.services.token;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.function.Function;

public interface JwtService {

    String generateToken(UserDetails userDetails);
    String generateToken(Map<String, Object> claims, UserDetails userDetails);
    String extractUsernameFromToken(String token);
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
    boolean isTokenValid(String token, UserDetails userDetails);

}
