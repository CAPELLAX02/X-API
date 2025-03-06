package com.x.backend.services.email;

import jakarta.mail.MessagingException;

public interface MailService {

    void sendActivationCodeViaEmail(String to, String username, String activationCode) throws MessagingException;
    void sendPasswordRecoveryCodeViaEmail(String to, String username, String activationCode) throws MessagingException;

}
