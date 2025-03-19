package com.x.backend.exceptions.auth;

import com.x.backend.exceptions.NotFoundException;

public class RefreshTokenNotFoundException extends NotFoundException {
    public RefreshTokenNotFoundException() {
        super("Refresh token not found");
    }
}
