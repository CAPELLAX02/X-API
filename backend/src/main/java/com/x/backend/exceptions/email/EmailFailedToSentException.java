package com.x.backend.exceptions.email;

import jakarta.mail.MessagingException;

public class EmailFailedToSentException extends MessagingException {
    public EmailFailedToSentException() {
        super("Email failed to sent");
    }
}
