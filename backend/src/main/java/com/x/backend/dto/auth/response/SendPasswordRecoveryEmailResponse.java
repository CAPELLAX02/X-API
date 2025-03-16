package com.x.backend.dto.auth.response;

import java.time.Instant;

public record SendPasswordRecoveryEmailResponse(
        Instant passwordRecoveryExpiry
) {
}
