package com.x.backend.exceptions.poll;

public class InvalidPollOptionIndexException extends RuntimeException {
    public InvalidPollOptionIndexException(int index) {
        super("Invalid poll option index: " + index);
    }
}
