package com.x.backend.exceptions.auth;

import com.x.backend.exceptions.BaseRuntimeException;

public class InvalidOrExpiredRefreshTokenException extends BaseRuntimeException {
    public InvalidOrExpiredRefreshTokenException() {
        super("Invalid or expired refresh token");
    }
}
