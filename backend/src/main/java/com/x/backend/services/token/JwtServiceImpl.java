package com.x.backend.services.token;

import com.x.backend.security.RSAKeyProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    private static final long ACCESS_TOKEN_EXPIRATION = 900; // 15 minutes
    private static final long REFRESH_TOKEN_EXPIRATION = 604800; // 7 days

    public JwtServiceImpl(RSAKeyProperties rsaKeyProperties) {
        this.privateKey = rsaKeyProperties.getPrivateKey();
        this.publicKey = rsaKeyProperties.getPublicKey();
    }

    @Override
    public String generateAccessToken(UserDetails userDetails) {
        return generateAccessToken(new HashMap<>(), userDetails);
    }

    @Override
    public String generateAccessToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(ACCESS_TOKEN_EXPIRATION)))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(REFRESH_TOKEN_EXPIRATION)))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    @Override
    public String extractUsernameFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
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

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsernameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
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
}
