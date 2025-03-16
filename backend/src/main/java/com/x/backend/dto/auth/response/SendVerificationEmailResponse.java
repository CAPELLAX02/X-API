package com.x.backend.dto.auth.response;

import java.time.Instant;

public record SendVerificationEmailResponse(
        Instant verificationCodeExpiry
) {
}
