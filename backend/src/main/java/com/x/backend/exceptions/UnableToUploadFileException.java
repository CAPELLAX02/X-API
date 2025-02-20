package com.x.backend.exceptions;

public class UnableToUploadFileException extends RuntimeException {
    public UnableToUploadFileException() {
        super("File upload failed");
    }
}
