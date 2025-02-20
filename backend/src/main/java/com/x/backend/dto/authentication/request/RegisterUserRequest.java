package com.x.backend.dto.authentication.request;

import java.sql.Date;

public record RegisterUserRequest(
        String firstName,
        String lastName,
        String email,
        Date dateOfBirth
) {
}
