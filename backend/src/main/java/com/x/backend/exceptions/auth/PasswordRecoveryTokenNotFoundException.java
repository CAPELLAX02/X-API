package com.x.backend.exceptions.auth;

import com.x.backend.exceptions.BaseNotFoundException;

public class PasswordRecoveryTokenNotFoundException extends BaseNotFoundException {
    public PasswordRecoveryTokenNotFoundException() {
        super("Password recovery token not found");
    }
}
