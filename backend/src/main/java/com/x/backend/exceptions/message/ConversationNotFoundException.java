package com.x.backend.exceptions.message;

import com.x.backend.exceptions.NotFoundException;

public class ConversationNotFoundException extends NotFoundException {
    public ConversationNotFoundException(Long id) {
        super("Conversation with ID: " + id + " not found.");
    }
}
