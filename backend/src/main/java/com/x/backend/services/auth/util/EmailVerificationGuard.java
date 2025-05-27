package com.x.backend.services.auth.util;

import com.x.backend.exceptions.auth.ExpiredVerificationCodeException;
import com.x.backend.exceptions.auth.InvalidVerificationCodeException;
import com.x.backend.exceptions.common.TooManyRequestsException;
import com.x.backend.models.user.ApplicationUser;

import java.time.Instant;

public class EmailVerificationGuard {

    public static void checkThrottleLimit(Instant expiryTime) {
        if (expiryTime != null && expiryTime.minusSeconds(240).isAfter(Instant.now())) {
            throw new TooManyRequestsException("Please wait at least 1 minute before requesting again.");
        }
    }

    public static void validateCode(ApplicationUser user, String inputCode) {
        if (user.getVerificationCode() == null || !user.getVerificationCode().equals(inputCode)) {
            throw new InvalidVerificationCodeException("Verification code does not match.");
        }
        if (user.getVerificationCodeExpiry() == null || user.getVerificationCodeExpiry().isBefore(Instant.now())) {
            throw new ExpiredVerificationCodeException("Verification code has expired.");
        }
    }

}
