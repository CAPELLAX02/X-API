package com.x.backend.exceptions.auth;

import com.x.backend.exceptions.NotFoundException;

public class PasswordRecoveryTokenNotFoundException extends NotFoundException {
    public PasswordRecoveryTokenNotFoundException() {
        super("Password recovery token not found");
    }
}
