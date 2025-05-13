package com.x.backend.services.token;

import com.x.backend.models.user.user.ApplicationUser;
import com.x.backend.security.rsa.RSAKeyProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service("jwtService")
public class JwtServiceImpl implements JwtService {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    private static final long ACCESS_TOKEN_EXPIRATION = 900; // 15 minutes
    private static final long REFRESH_TOKEN_EXPIRATION = 604800; // 7 days

    public JwtServiceImpl(RSAKeyProperties rsaKeyProperties) {
        this.privateKey = rsaKeyProperties.getPrivateKey();
        this.publicKey = rsaKeyProperties.getPublicKey();
    }

    // === TOKEN GENERATION === //

    @Override
    public String generateAccessToken(ApplicationUser user) {
        Map<String, Object> claims = buildStandardClaims(user);
        claims.put("token_type", "access");
        claims.put("jti", UUID.randomUUID().toString());
        return buildToken(claims, user.getUsername(), ACCESS_TOKEN_EXPIRATION);
    }

    @Override
    public String generateRefreshToken(ApplicationUser user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("token_type", "refresh");
        claims.put("jti", UUID.randomUUID().toString());
        return buildToken(claims, user.getUsername(), REFRESH_TOKEN_EXPIRATION);
    }

    private Map<String, Object> buildStandardClaims(ApplicationUser user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", user.getId());
        claims.put("username", user.getUsername());
        claims.put("email", user.getEmail());
        claims.put("nickname", user.getNickname());
        claims.put("roles", user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));
        return claims;
    }

    private String buildToken(Map<String, Object> claims, String subject, long expiresInSeconds) {
        Instant now = Instant.now();
        String jti = (String) claims.get("jti");

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(expiresInSeconds)))
                .setId(jti)
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    // === CLAIM EXTRACTION === //

    @Override
    public String extractUsernameFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public Long extractUserIdFromToken(String token) {
        return extractClaim(token, claims -> claims.get("uid", Long.class));
    }

    @Override
    public List<String> extractRoles(String token) {
        return extractClaim(token, claims -> {
            Object rolesObj = claims.get("roles");
            if (rolesObj instanceof List<?> rolesList) {
                return rolesList.stream().map(Object::toString).collect(Collectors.toList());
            }
            return List.of();
        });
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        final Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            throw new JwtException("Invalid JWT: " + e.getMessage());
        }
    }

    // === VALIDATION === //

    @Override
    public boolean isTokenValid(String token, ApplicationUser user) {
        String username = extractUsernameFromToken(token);
        return username.equals(user.getUsername()) && !isTokenExpired(token);
    }

    @Override
    public boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    @Override
    public long getExpirationFromToken(String token) {
        Date expiration = extractClaim(token, Claims::getExpiration);
        return (expiration.getTime() - System.currentTimeMillis()) / 1000;
    }

    @Override
    public String extractJtiFromToken(String token) {
        return extractClaim(token, Claims::getId);
    }

}
