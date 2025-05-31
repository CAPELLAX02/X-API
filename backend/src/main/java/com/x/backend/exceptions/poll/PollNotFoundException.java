package com.x.backend.exceptions.poll;

import com.x.backend.exceptions.BaseNotFoundException;

public class PollNotFoundException extends BaseNotFoundException {
    public PollNotFoundException(Long id) {
        super("Poll with ID: " + id + " not found.");
    }
}
