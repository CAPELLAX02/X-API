package com.x.backend.exceptions;

public class InvalidImagePrefixException extends RuntimeException {
    public InvalidImagePrefixException() {
        super("Invalid image prefix. Available prefix options: [\"profile_picture\", \"banner_picture\"]");
    }
}
