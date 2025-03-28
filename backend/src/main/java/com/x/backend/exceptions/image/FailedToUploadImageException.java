package com.x.backend.exceptions.image;

public class FailedToUploadImageException extends RuntimeException {
    public FailedToUploadImageException(String message) {
        super(message);
    }
}
