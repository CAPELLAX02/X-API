package com.x.backend.exceptions.notification;

import com.x.backend.exceptions.BaseNotFoundException;

public class NotificationNotFoundException extends BaseNotFoundException {
    public NotificationNotFoundException(Long id) {
        super("Notification with ID: " + id + " not found.");
    }
}
