package com.x.backend.services.email;

import com.x.backend.exceptions.email.EmailFailedToSentException;

public interface MailService {

    void sendVerificationCodeViaEmail(String to, String username, String verificationCode) throws EmailFailedToSentException;
    void sendPasswordRecoveryCodeViaEmail(String to, String username, String passwordRecoveryCode) throws EmailFailedToSentException;

}
