package com.x.backend.services.email;

import jakarta.mail.MessagingException;

public interface EmailService {

    void sendActivationCodeViaEmail(String to, String username, String activationCode) throws MessagingException;
    void sendPasswordRecoveryCodeViaEmail(String to, String username, String activationCode) throws MessagingException;

}
