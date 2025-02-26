package com.x.backend.exceptions;

public class MessageNotFoundException extends NotFoundException {
    public MessageNotFoundException(Long id) {
        super("Message with ID: " + id + " not found.");
    }
}
