package com.x.backend.exceptions.image;

import com.x.backend.exceptions.BaseRuntimeException;

public class FailedToUploadImageException extends BaseRuntimeException {
    public FailedToUploadImageException(String message) {
        super(message);
    }
}
