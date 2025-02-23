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

    @ExceptionHandler(ImageNotFoundException.class)
    public ResponseEntity<Object> handleImageNotFoundException(ImageNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ UnableToUploadFileException.class, UnableToDownloadFileException.class })
    public ResponseEntity<Object> handlePhotoExceptions() {
        return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(InvalidImagePrefixException.class)
    public ResponseEntity<Object> handleInvalidImagePrefixException(InvalidImagePrefixException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CannotFollowThemselvesException.class)
    public ResponseEntity<Object> handleCannotFollowThemselvesException(CannotFollowThemselvesException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<Object> handlePostNotFoundException(PostNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnableToCreatePostException.class)
    public ResponseEntity<Object> handleUnableToCreatePostException(UnableToCreatePostException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidPollEndDateException.class)
    public ResponseEntity<Object> handleInvalidPollEndDateException(InvalidPollEndDateException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PollChoiceNotFoundException.class)
    public ResponseEntity<Object> handlePollChoiceNotFoundException(PollChoiceNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PollExpiredException.class)
    public ResponseEntity<Object> handlePollExpiredException(PollExpiredException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

}
