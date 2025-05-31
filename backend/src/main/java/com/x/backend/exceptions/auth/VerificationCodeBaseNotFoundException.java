package com.x.backend.exceptions.auth;

import com.x.backend.exceptions.BaseNotFoundException;

public class VerificationCodeBaseNotFoundException extends BaseNotFoundException {
    public VerificationCodeBaseNotFoundException() {
        super("Verification code not found.");
    }
}
