package com.x.backend.exceptions.notification;

import com.x.backend.exceptions.NotFoundException;

public class NotificationNotFoundException extends NotFoundException {
    public NotificationNotFoundException(Long id) {
        super("Notification with ID: " + id + " not found.");
    }
}
