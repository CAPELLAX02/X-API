package com.x.backend.exceptions.auth;

public class InvalidLoginRequestKeyException extends IllegalArgumentException {
    public InvalidLoginRequestKeyException() {
        super("Invalid login request key. Available keys are: [\"email\", \"username\", \"phone\"]");
    }
}
