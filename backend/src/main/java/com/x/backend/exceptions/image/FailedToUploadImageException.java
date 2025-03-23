package com.x.backend.exceptions.image;

public class FailedToUploadImageException extends RuntimeException {
    public FailedToUploadImageException() {
        super("Image upload failed.");
    }
}
