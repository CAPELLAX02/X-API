package com.x.backend.utils.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Map;

/**
 * Standardized API response wrapper for HTTP-based RESTful services.
 * <p>
 * Designed to provide consistency across all controller responses, this wrapper conveys:
 * <ul>
 *     <li>Success or failure state</li>
 *     <li>HTTP status code</li>
 *     <li>Human-readable status message</li>
 *     <li>Optional data payload</li>
 *     <li>Optional error map for validation or field-level issues</li>
 *     <li>UTC-based response timestamp</li>
 * </ul>
 *
 * <p><strong>Recommended usage:</strong></p>
 * <ul>
 *     <li>Use {@code successOk()} for successful GET, PUT, or PATCH operations.</li>
 *     <li>Use {@code successCreated()} for POST requests where a resource has been created.</li>
 *     <li>Use {@code successAccepted()} for asynchronous or deferred processing responses (e.g., email dispatch).</li>
 *     <li>Use {@code error()} for error responses with optional field-specific error detail.</li>
 * </ul>
 *
 * @param <T> the type of payload being returned in the response
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class BaseApiResponse<T> {

    private final boolean success;
    private final HttpStatus status;
    private final String message;
    private T data;
    private Map<String, String> errors;
    private final Instant timestamp;

    /**
     * Constructs a successful response with payload, message, and HTTP status.
     *
     * @param data    the data returned by the operation
     * @param message a user-friendly message
     * @param status  a 2xx HTTP status indicating success
     */
    public BaseApiResponse(T data, String message, HttpStatus status) {
        this.success = status.is2xxSuccessful();
        this.status = status;
        this.message = message;
        this.data = data;
        this.timestamp = Instant.now();
    }

    /**
     * Constructs a failure response with message and HTTP status.
     *
     * @param errorMessage explanation of the failure
     * @param status       non-2xx HTTP status code
     */
    public BaseApiResponse(String errorMessage, HttpStatus status) {
        this.success = false;
        this.status = status;
        this.message = errorMessage;
        this.timestamp = Instant.now();
    }

    /**
     * Constructs a failure response with detailed error messages.
     *
     * @param errorMessage general error explanation
     * @param status       non-2xx HTTP status code
     * @param errors       field-specific validation errors
     */
    public BaseApiResponse(String errorMessage, HttpStatus status, Map<String, String> errors) {
        this.success = false;
        this.status = status;
        this.message = errorMessage;
        this.errors = errors;
        this.timestamp = Instant.now();
    }

    // ======================================= 200 OK ==================================================================

    /**
     * Indicates a successful, synchronous operation with data and custom message.
     * <p>Use this for GET, PUT, PATCH or DELETE operations where the result is known and immediate.</p>
     *
     * @param data    the response payload
     * @param message descriptive success message
     */
    public static <T> BaseApiResponse<T> successOk(T data, String message) {
        return new BaseApiResponse<>(data, message, HttpStatus.OK);
    }

    /**
     * Indicates a successful operation with only a message.
     * <p>Use when there is no need to return a payload (e.g., "Profile updated").</p>
     */
    public static <T> BaseApiResponse<T> successOk(String message) {
        return new BaseApiResponse<>(null, message, HttpStatus.OK);
    }

    /**
     * Indicates a successful operation with only data and a default message.
     */
    public static <T> BaseApiResponse<T> successOk(T data) {
        return new BaseApiResponse<>(data, "Success", HttpStatus.OK);
    }

    // ======================================= 201 Created =============================================================

    /**
     * Indicates successful creation of a resource (e.g., user, post, record).
     * <p>Use for POST operations that persist a new entity.</p>
     *
     * @param data    the newly created resource
     * @param message descriptive creation message
     */
    public static <T> BaseApiResponse<T> successCreated(T data, String message) {
        return new BaseApiResponse<>(data, message, HttpStatus.CREATED);
    }

    /**
     * Indicates successful creation without payload (e.g., creation acknowledged, no details required).
     */
    public static <T> BaseApiResponse<T> successCreated(String message) {
        return new BaseApiResponse<>(null, message, HttpStatus.CREATED);
    }

    /**
     * Indicates successful creation of a resource with default message.
     */
    public static <T> BaseApiResponse<T> successCreated(T data) {
        return new BaseApiResponse<>(data, "Resource created successfully", HttpStatus.CREATED);
    }

    // ======================================= 202 Accepted ============================================================

    /**
     * Indicates that the request has been accepted for asynchronous processing.
     * <p>Use this when the operation will be executed later (e.g., background job, email sending, queued actions).</p>
     *
     * @param data    optional tracking or acknowledgment payload
     * @param message explanation of acceptance
     */
    public static <T> BaseApiResponse<T> successAccepted(T data, String message) {
        return new BaseApiResponse<>(data, message, HttpStatus.ACCEPTED);
    }

    /**
     * Indicates accepted asynchronous processing without payload.
     */
    public static <T> BaseApiResponse<T> successAccepted(String message) {
        return new BaseApiResponse<>(null, message, HttpStatus.ACCEPTED);
    }

    /**
     * Indicates accepted asynchronous processing with default message.
     */
    public static <T> BaseApiResponse<T> successAccepted(T data) {
        return new BaseApiResponse<>(data, "Request accepted for processing", HttpStatus.ACCEPTED);
    }

    // ======================================= Legacy Success (200 OK overloads) =======================================

    /**
     * General success method allowing explicit HTTP status specification.
     * <p>Should be used cautiously and explicitly to match HTTP semantics.</p>
     *
     * @param data    response body
     * @param message client-friendly explanation
     * @param status  must be a 2xx HTTP status
     */
    @Deprecated
    public static <T> BaseApiResponse<T> success(T data, String message, HttpStatus status) {
        return new BaseApiResponse<>(data, message, status);
    }

    /**
     * Legacy: success response with message and no data (defaults to 200 OK).
     */
    @Deprecated
    public static <T> BaseApiResponse<T> success(String message) {
        return new BaseApiResponse<>(null, message, HttpStatus.OK);
    }

    /**
     * Legacy: success response with data and message (defaults to 200 OK).
     */
    @Deprecated
    public static <T> BaseApiResponse<T> success(T data, String message) {
        return new BaseApiResponse<>(data, message, HttpStatus.OK);
    }

    /**
     * Legacy: success response with data and default message (defaults to 200 OK).
     */
    @Deprecated
    public static <T> BaseApiResponse<T> success(T data) {
        return new BaseApiResponse<>(data, "Success", HttpStatus.OK);
    }

    // ======================================= Error ===================================================================

    /**
     * Constructs a general error response without field-specific details.
     *
     * @param errorMessage human-readable explanation
     * @param status       4xx or 5xx status code
     */
    public static <T> BaseApiResponse<T> error(String errorMessage, HttpStatus status) {
        return new BaseApiResponse<>(errorMessage, status);
    }

    /**
     * Constructs a validation or business-rule error response with field-level explanations.
     *
     * @param errorMessage general message
     * @param status       4xx HTTP status
     * @param errors       map of field names to error messages
     */
    public static <T> BaseApiResponse<T> error(String errorMessage, HttpStatus status, Map<String, String> errors) {
        return new BaseApiResponse<>(errorMessage, status, errors);
    }

    // ======================================= Getters =================================================================

    /** @return true if the HTTP status is a 2xx code */
    public boolean isSuccess() {
        return success;
    }

    /** @return HTTP status code of the response */
    public HttpStatus getStatus() {
        return status;
    }

    /** @return short message intended for clients */
    public String getMessage() {
        return message;
    }

    /** @return the data payload, if applicable */
    public T getData() {
        return data;
    }

    /** @return field-level error map (only for failed responses) */
    public Map<String, String> getErrors() {
        return errors;
    }

    /** @return UTC timestamp representing the time of response creation */
    public Instant getTimestamp() {
        return timestamp;
    }

}
