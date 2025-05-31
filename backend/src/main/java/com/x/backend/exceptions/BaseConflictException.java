package com.x.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class BaseConflictException extends RuntimeException {
    public BaseConflictException(String message) {
        super(message);
    }
}
