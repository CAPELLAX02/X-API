package com.x.backend.exceptions;

public class InvalidOrExpiredVerificationCode extends RuntimeException {
    public InvalidOrExpiredVerificationCode() {
        super("Invalid or expired verification code");
    }
}
