package com.x.backend.exceptions.message;

import com.x.backend.exceptions.BaseNotFoundException;

public class MessageNotFoundException extends BaseNotFoundException {
    public MessageNotFoundException(Long id) {
        super("Message with ID: " + id + " not found.");
    }
}
