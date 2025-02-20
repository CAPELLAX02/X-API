package com.x.backend.exceptions;

public class UnableToDownloadFileException extends RuntimeException {
    public UnableToDownloadFileException() {
        super("File download failed");
    }
}
