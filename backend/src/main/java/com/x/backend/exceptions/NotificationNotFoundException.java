package com.x.backend.exceptions;

public class NotificationNotFoundException extends NotFoundException {
    public NotificationNotFoundException(Long id) {
        super("Notification with ID: " + id + " not found.");
    }
}
