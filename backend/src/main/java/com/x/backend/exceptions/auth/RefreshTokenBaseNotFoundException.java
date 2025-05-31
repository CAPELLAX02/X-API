package com.x.backend.exceptions.auth;

import com.x.backend.exceptions.BaseNotFoundException;

public class RefreshTokenBaseNotFoundException extends BaseNotFoundException {
    public RefreshTokenBaseNotFoundException() {
        super("Refresh token not found");
    }
}
