package com.x.backend.exceptions;

import jakarta.mail.MessagingException;

public class EmailFailedToSentException extends MessagingException {
    public EmailFailedToSentException() {
        super("The email failed to sent, please try again later.");
    }
}
