package com.x.backend.exceptions.message;

import com.x.backend.exceptions.BaseNotFoundException;

public class ConversationBaseNotFoundException extends BaseNotFoundException {
    public ConversationBaseNotFoundException(Long id) {
        super("Conversation with ID: " + id + " not found.");
    }
}
