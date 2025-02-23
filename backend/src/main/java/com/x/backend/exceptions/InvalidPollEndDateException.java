package com.x.backend.exceptions;

public class InvalidPollEndDateException extends RuntimeException {
    public InvalidPollEndDateException() {
        super("End date of a poll cannot be in the past");
    }
}
