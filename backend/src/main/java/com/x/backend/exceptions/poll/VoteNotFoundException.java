package com.x.backend.exceptions.poll;

import com.x.backend.exceptions.NotFoundException;

public class VoteNotFoundException extends NotFoundException {
    public VoteNotFoundException(Long id) {
        super("Vote with ID: " + id + " not found.");
    }
}
