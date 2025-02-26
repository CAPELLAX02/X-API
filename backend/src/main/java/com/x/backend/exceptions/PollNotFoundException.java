package com.x.backend.exceptions;

public class PollNotFoundException extends NotFoundException {
    public PollNotFoundException(Long id) {
        super("Poll with ID: " + id + " not found.");
    }
}
