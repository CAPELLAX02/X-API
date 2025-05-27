package com.x.backend.services.auth.util;

import com.x.backend.exceptions.email.EmailFailedToSentException;
import com.x.backend.services.email.MailService;

public class EmailDispatcher {

    public static void sendVerificationEmail(MailService mailService, String email, String fullName, String code)
            throws EmailFailedToSentException
    {
        mailService.sendVerificationCodeViaEmail(email, fullName, code);
    }

    public static void sendPasswordRecoveryEmail(MailService mailService, String email, String fullName, String code)
            throws EmailFailedToSentException
    {
        mailService.sendPasswordRecoveryCodeViaEmail(email, fullName, code);
    }

}
