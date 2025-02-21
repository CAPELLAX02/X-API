package com.x.backend.exceptions;

public class CannotFollowThemselvesException extends IllegalArgumentException {
    public CannotFollowThemselvesException() {
        super("Users cannot follow themselves.");
    }
}
