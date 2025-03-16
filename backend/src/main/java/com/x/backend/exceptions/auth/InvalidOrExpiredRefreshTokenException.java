package com.x.backend.exceptions.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidOrExpiredRefreshTokenException extends RuntimeException {
    public InvalidOrExpiredRefreshTokenException() {
        super("Invalid or expired refresh token");
    }
}
