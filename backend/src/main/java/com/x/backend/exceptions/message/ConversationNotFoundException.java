package com.x.backend.exceptions.message;

import com.x.backend.exceptions.BaseNotFoundException;

public class ConversationNotFoundException extends BaseNotFoundException {
    public ConversationNotFoundException(Long id) {
        super("Conversation with ID: " + id + " not found.");
    }
}
