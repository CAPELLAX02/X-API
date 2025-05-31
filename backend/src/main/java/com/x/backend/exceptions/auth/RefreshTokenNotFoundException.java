package com.x.backend.exceptions.auth;

import com.x.backend.exceptions.BaseNotFoundException;

public class RefreshTokenNotFoundException extends BaseNotFoundException {
    public RefreshTokenNotFoundException() {
        super("Refresh token not found");
    }
}
