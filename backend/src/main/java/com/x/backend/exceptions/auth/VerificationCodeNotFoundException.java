package com.x.backend.exceptions.auth;

import com.x.backend.exceptions.NotFoundException;

public class VerificationCodeNotFoundException extends NotFoundException {
    public VerificationCodeNotFoundException() {
        super("Verification code not found.");
    }
}
