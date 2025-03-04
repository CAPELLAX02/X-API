package com.x.backend.dto;

import com.x.backend.exceptions.auth.InvalidLoginRequestKeyException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(

        String email,
        String username,
        String phoneNumber,

        @NotBlank(message = "Password is required.")
        @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters.")
        String password

) {

    public boolean isValid() {
        return (email != null && !email.isBlank())
                || (username!= null && !username.isBlank())
                || (phoneNumber != null && !phoneNumber.isBlank());
    }

    public String getLoginKey() {
        if (email != null && !email.isBlank())
            return email;
        if (username != null && !username.isBlank())
            return username;
        if (phoneNumber != null && !phoneNumber.isBlank())
            return phoneNumber;
        throw new InvalidLoginRequestKeyException();
    }

}
