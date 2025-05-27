package com.x.backend.exceptions.message;

import com.x.backend.exceptions.AlreadyExistsException;

public class MessageAlreadyMarkedAsReadException extends AlreadyExistsException {
    public MessageAlreadyMarkedAsReadException() {
        super("Message already marked as read.");
    }
}
