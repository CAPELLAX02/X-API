package com.x.backend.utils.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseApiResponse<T> {

    private final boolean success;
    private final HttpStatus status;
    private final String message;
    private T data;
    private Map<String, String> errors;
    private final LocalDateTime timestamp;

    public BaseApiResponse(T data, String message, HttpStatus status) {
        this.success = status.is2xxSuccessful();
        this.status = status;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public BaseApiResponse(String errorMessage, HttpStatus status) {
        this.success = false;
        this.status = status;
        this.message = errorMessage;
        this.timestamp = LocalDateTime.now();
    }

    public BaseApiResponse(String errorMessage, HttpStatus status, Map<String, String> errors) {
        this.success = false;
        this.status = status;
        this.message = errorMessage;
        this.errors = errors;
        this.timestamp = LocalDateTime.now();
    }

    public static <T> BaseApiResponse<T> success(T data, String message, HttpStatus status) {
        return new BaseApiResponse<>(data, message, status);
    }

    public static <T> BaseApiResponse<T> success(T data, String message) {
        return new BaseApiResponse<>(data, message, HttpStatus.OK);
    }

    public static <T> BaseApiResponse<T> success(String message) {
        return new BaseApiResponse<>(message, HttpStatus.OK);
    }

    public static <T> BaseApiResponse<T> success(T data) {
        return new BaseApiResponse<>(data, "Success", HttpStatus.OK);
    }

    public static <T> BaseApiResponse<T> error(String errorMessage, HttpStatus status) {
        return new BaseApiResponse<>(errorMessage, status);
    }

    public static <T> BaseApiResponse<T> error(String errorMessage, HttpStatus status, Map<String, String> errors) {
        return new BaseApiResponse<>(errorMessage, status, errors);
    }

    public boolean isSuccess() {
        return success;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
