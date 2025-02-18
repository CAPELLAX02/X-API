package com.x.backend.dto.request;

import java.sql.Date;

public record RegistrationRequest(
        String firstName,
        String lastName,
        String email,
        Date dateOfBirth
) {
    @Override
    public String toString() {
        return "RegistrationRequest{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }
}
