package com.x.backend.exceptions.image;

import com.x.backend.exceptions.CustomRuntimeException;

public class FailedToUploadImageException extends CustomRuntimeException {
    public FailedToUploadImageException(String message) {
        super(message);
    }
}
