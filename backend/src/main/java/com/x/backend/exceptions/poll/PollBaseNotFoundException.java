package com.x.backend.exceptions.poll;

import com.x.backend.exceptions.BaseNotFoundException;

public class PollBaseNotFoundException extends BaseNotFoundException {
    public PollBaseNotFoundException(Long id) {
        super("Poll with ID: " + id + " not found.");
    }
}
