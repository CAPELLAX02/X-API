package com.x.backend.services.auth.util;

import com.x.backend.exceptions.common.TooManyRequestsException;

import java.time.Instant;

public class EmailResendGuard {

    public static void checkThrottleLimit(Instant expiryTime) {
        if (expiryTime != null && expiryTime.minusSeconds(240).isAfter(Instant.now())) {
            throw new TooManyRequestsException("Please wait at least 1 minute before requesting again.");
        }
    }

}
