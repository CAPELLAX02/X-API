package com.x.backend.exceptions.auth;

import com.x.backend.exceptions.BaseNotFoundException;

public class PasswordRecoveryTokenBaseNotFoundException extends BaseNotFoundException {
    public PasswordRecoveryTokenBaseNotFoundException() {
        super("Password recovery token not found");
    }
}
