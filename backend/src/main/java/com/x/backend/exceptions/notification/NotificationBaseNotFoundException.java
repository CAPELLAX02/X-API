package com.x.backend.exceptions.notification;

import com.x.backend.exceptions.BaseNotFoundException;

public class NotificationBaseNotFoundException extends BaseNotFoundException {
    public NotificationBaseNotFoundException(Long id) {
        super("Notification with ID: " + id + " not found.");
    }
}
