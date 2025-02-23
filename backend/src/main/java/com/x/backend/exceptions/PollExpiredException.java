package com.x.backend.exceptions;

public class PollExpiredException extends RuntimeException {
    public PollExpiredException() {
        super("Poll expired");
    }
}
