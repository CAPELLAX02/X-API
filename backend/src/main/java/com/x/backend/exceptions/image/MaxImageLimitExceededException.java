package com.x.backend.exceptions.image;

import com.x.backend.exceptions.CustomRuntimeException;

public class MaxImageLimitExceededException extends CustomRuntimeException {
    public MaxImageLimitExceededException(int limit) {
        super("A post cannot contain more than " + limit + " images.");
    }
}
