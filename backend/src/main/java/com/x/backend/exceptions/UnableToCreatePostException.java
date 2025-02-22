package com.x.backend.exceptions;

public class UnableToCreatePostException extends RuntimeException {
    public UnableToCreatePostException() {
        super("Unable to create post");
    }
}
