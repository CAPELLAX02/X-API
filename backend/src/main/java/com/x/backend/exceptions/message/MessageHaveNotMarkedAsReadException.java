package com.x.backend.exceptions.message;

import com.x.backend.exceptions.AlreadyExistsException;

public class MessageHaveNotMarkedAsReadException extends AlreadyExistsException {
    public MessageHaveNotMarkedAsReadException() {
        super("Message already marked as read.");
    }
}
