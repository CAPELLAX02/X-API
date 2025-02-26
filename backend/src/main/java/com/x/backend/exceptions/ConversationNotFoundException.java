package com.x.backend.exceptions;

public class ConversationNotFoundException extends NotFoundException {
    public ConversationNotFoundException(Long id) {
        super("Conversation with ID: " + id + " not found.");
    }
}
