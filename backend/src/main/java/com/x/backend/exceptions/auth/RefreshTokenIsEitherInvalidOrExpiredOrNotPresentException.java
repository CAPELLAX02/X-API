package com.x.backend.exceptions.auth;

public class RefreshTokenIsEitherInvalidOrExpiredOrNotPresentException extends RuntimeException {
    public RefreshTokenIsEitherInvalidOrExpiredOrNotPresentException() {
        super("Refresh token is either expired or invalid or expired.");
    }
}
