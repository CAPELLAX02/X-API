package com.x.backend.exceptions.message;

import com.x.backend.exceptions.NotFoundException;

public class MessageNotFoundException extends NotFoundException {
    public MessageNotFoundException(Long id) {
        super("Message with ID: " + id + " not found.");
    }
}
