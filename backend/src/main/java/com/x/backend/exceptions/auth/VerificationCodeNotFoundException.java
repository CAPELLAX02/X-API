package com.x.backend.exceptions.auth;

import com.x.backend.exceptions.BaseNotFoundException;

public class VerificationCodeNotFoundException extends BaseNotFoundException {
    public VerificationCodeNotFoundException() {
        super("Verification code not found.");
    }
}
