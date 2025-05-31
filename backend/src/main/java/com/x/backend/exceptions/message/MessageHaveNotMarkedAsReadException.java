package com.x.backend.exceptions.message;

import com.x.backend.exceptions.BaseConflictException;

public class MessageHaveNotMarkedAsReadException extends BaseConflictException {
    public MessageHaveNotMarkedAsReadException() {
        super("Message already marked as read.");
    }
}
