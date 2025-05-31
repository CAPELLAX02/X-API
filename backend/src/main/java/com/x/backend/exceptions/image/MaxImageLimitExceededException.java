package com.x.backend.exceptions.image;

import com.x.backend.exceptions.BaseRuntimeException;

public class MaxImageLimitExceededException extends BaseRuntimeException {
    public MaxImageLimitExceededException(int limit) {
        super("A post cannot contain more than " + limit + " images.");
    }
}
