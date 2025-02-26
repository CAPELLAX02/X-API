package com.x.backend.utils.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseApiResponse<T> {

    private boolean success;
    private HttpStatus status;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    public BaseApiResponse(T data, String message, HttpStatus status) {
        this.success = true;
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

    public static <T> BaseApiResponse<T> success(T data, String message) {
        return new BaseApiResponse<>(data, message, HttpStatus.OK);
    }

    public static <T> BaseApiResponse<T> error(String errorMessage, HttpStatus status) {
        return new BaseApiResponse<>(errorMessage, status);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

}
