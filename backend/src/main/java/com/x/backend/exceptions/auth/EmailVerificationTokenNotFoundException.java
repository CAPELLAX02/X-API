package com.x.backend.exceptions.auth;

import com.x.backend.exceptions.BaseNotFoundException;

public class EmailVerificationTokenNotFoundException extends BaseNotFoundException {
    public EmailVerificationTokenNotFoundException() {
        super("Email verification token not found");
    }
}
