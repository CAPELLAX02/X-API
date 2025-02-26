package com.x.backend.services.token;

import com.x.backend.config.security.RSAKeyProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@Transactional
public class JwtServiceImpl implements JwtService {

    private final RSAKeyProperties rsaKeyProperties;

    public JwtServiceImpl(RSAKeyProperties rsaKeyProperties) {
        this.rsaKeyProperties = rsaKeyProperties;
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    @Override
    public String generateToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(86400)))
                .signWith(rsaKeyProperties.getPrivateKey(), SignatureAlgorithm.RS256)
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
        return Jwts.parserBuilder().build().parseClaimsJws(token).getBody();
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsernameFromToken(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

}
