package com.x.backend.services;

import org.springframework.security.core.Authentication;

public interface JwtService {

    String generateToken(Authentication authentication);
    String getUsernameFromToken(String token);

}
