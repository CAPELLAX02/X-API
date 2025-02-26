package com.x.backend.exceptions.poll;

import com.x.backend.exceptions.NotFoundException;

public class PollNotFoundException extends NotFoundException {
    public PollNotFoundException(Long id) {
        super("Poll with ID: " + id + " not found.");
    }
}
