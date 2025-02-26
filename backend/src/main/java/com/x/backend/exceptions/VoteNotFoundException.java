package com.x.backend.exceptions;

public class VoteNotFoundException extends NotFoundException {
    public VoteNotFoundException(Long id) {
        super("Vote with ID: " + id + " not found.");
    }
}
