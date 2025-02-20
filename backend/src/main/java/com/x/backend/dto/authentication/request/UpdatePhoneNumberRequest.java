package com.x.backend.dto.authentication.request;

public record UpdatePhoneNumberRequest(
        String username,
        String phoneNumber
) {
}
