package com.x.backend.exceptions;

import com.x.backend.dto.user.response.UserResponse;
import com.x.backend.utils.api.BaseApiResponse;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<BaseApiResponse<Object>> handleAccessDeniedException(AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BaseApiResponse.error(e.getMessage() != null ? "You do not have permission to access this resource or perform this action" : e.getMessage(), HttpStatus.FORBIDDEN));
    }

    @ExceptionHandler(BaseRuntimeException.class)
    public ResponseEntity<BaseApiResponse<String>> handleCustomRuntimeException(BaseRuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseApiResponse.error(e.getMessage(), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(BaseConflictException.class)
    public ResponseEntity<BaseApiResponse<UserResponse>> handleAlreadyExistsException(BaseConflictException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(BaseApiResponse.error(e.getMessage(), HttpStatus.CONFLICT));
    }

    @ExceptionHandler(BaseNotFoundException.class)
    public ResponseEntity<BaseApiResponse<String>> handleNotFoundException(BaseNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseApiResponse.error(e.getMessage(), HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<BaseApiResponse<String>> handleUsernameNotFoundException(UsernameNotFoundException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(BaseApiResponse.error("User not found with the provided username", HttpStatus.UNAUTHORIZED));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BaseApiResponse<String>> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseApiResponse.error(e.getMessage(), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<BaseApiResponse<Object>> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseApiResponse.error(e.getMessage(), HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseApiResponse<Map<String, String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseApiResponse.error("Validation failed", HttpStatus.BAD_REQUEST, errors));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<BaseApiResponse<Void>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseApiResponse.error("Invalid request format. Please make sure dateOfBirth is in ISO 8601 format (yyyy-MM-dd).", HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BaseApiResponse<Map<String, String>>> handleConstraintViolationException(ConstraintViolationException e) {
        Map<String, String> errors = new HashMap<>();
        e.getConstraintViolations().forEach(constraintViolation -> errors.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseApiResponse.error("Validation failed", HttpStatus.BAD_REQUEST, errors));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<BaseApiResponse<Object>> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseApiResponse.error("Missing required servlet parameter: " + e.getParameterName(), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<BaseApiResponse<Object>> handleBindException(BindException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseApiResponse.error("Invalid value for field: " + Objects.requireNonNull(e.getFieldError()).getField(), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<BaseApiResponse<Object>> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(BaseApiResponse.error("Database integrity constraint violation", HttpStatus.CONFLICT));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<BaseApiResponse<Object>> handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(BaseApiResponse.error("Provided HTTP method not supported", HttpStatus.METHOD_NOT_ALLOWED));
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<BaseApiResponse<Object>> handleMessagingException(MessagingException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BaseApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
    }

}
