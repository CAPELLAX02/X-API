package com.x.backend.exceptions.message;

import com.x.backend.exceptions.BaseConflictException;

public class MessageAlreadyMarkedAsReadException extends BaseConflictException {
    public MessageAlreadyMarkedAsReadException() {
        super("Message already marked as read.");
    }
}
