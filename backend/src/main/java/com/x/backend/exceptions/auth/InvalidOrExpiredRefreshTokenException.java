package com.x.backend.exceptions.auth;

import com.x.backend.exceptions.CustomRuntimeException;

public class InvalidOrExpiredRefreshTokenException extends CustomRuntimeException {
    public InvalidOrExpiredRefreshTokenException() {
        super("Invalid or expired refresh token");
    }
}
