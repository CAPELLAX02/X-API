package com.x.backend.exceptions.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidLoginRequestKeyException extends IllegalArgumentException {
    public InvalidLoginRequestKeyException() {
        super("Invalid login request key. Available keys are: [\"email\", \"username\", \"phone\"]");
    }
}
