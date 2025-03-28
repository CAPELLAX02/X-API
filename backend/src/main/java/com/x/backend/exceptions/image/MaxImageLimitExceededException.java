package com.x.backend.exceptions.image;

public class MaxImageLimitExceededException extends RuntimeException {
    public MaxImageLimitExceededException(int limit) {
        super("A post cannot contain more than " + limit + " images.");
    }
}
