package com.x.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyTakenException.class)
    public ResponseEntity<Object> handleEmailAlreadyTakenException(EmailAlreadyTakenException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserDoesNotExistException.class)
    public ResponseEntity<Object> handleUserDoesNotExistException(UserDoesNotExistException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailFailedToSentException.class)
    public ResponseEntity<Object> handleEmailFailedToSentException(EmailFailedToSentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(InvalidOrExpiredVerificationCode.class)
    public ResponseEntity<Object> handleInvalidOrExpiredVerificationCode(InvalidOrExpiredVerificationCode e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
