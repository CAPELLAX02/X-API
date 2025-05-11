package com.x.backend.exceptions.poll;

public class PollHasExpiredException extends RuntimeException {
    public PollHasExpiredException() {
        super("This poll has expired.");
    }
}
