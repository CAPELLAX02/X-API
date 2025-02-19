package com.x.backend.services;

import com.x.backend.exceptions.EmailFailedToSentException;

public interface MailService {

    void sendVerificationCodeViaEmail(String to, String verificationCode) throws EmailFailedToSentException;

}
