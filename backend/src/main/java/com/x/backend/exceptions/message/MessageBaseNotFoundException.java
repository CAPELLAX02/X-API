package com.x.backend.exceptions.message;

import com.x.backend.exceptions.BaseNotFoundException;

public class MessageBaseNotFoundException extends BaseNotFoundException {
    public MessageBaseNotFoundException(Long id) {
        super("Message with ID: " + id + " not found.");
    }
}
