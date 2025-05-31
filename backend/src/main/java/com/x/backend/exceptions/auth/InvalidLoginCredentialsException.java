package com.x.backend.exceptions.auth;

import com.x.backend.exceptions.BaseRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidLoginCredentialsException extends BaseRuntimeException {
    public InvalidLoginCredentialsException() {
        super("Invalid login credentials");
    }
}
