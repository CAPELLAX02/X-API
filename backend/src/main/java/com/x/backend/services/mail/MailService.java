package com.x.backend.services.mail;

import com.x.backend.exceptions.EmailFailedToSentException;

public interface MailService {

    void sendVerificationCodeViaEmail(String to, String verificationCode) throws EmailFailedToSentException;

}
