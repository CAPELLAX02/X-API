package com.x.backend.services.email;

import com.x.backend.exceptions.email.EmailFailedToSentException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@Transactional
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    public MailServiceImpl(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendActivationCodeViaEmail(String to, String username, String activationCode) throws MessagingException {
        sendEmailWithTemplate(to, username, activationCode, "Your Verification Code", "activation-email-template.html");
    }

    @Override
    public void sendPasswordRecoveryCodeViaEmail(String to, String username, String activationCode) throws MessagingException {
        sendEmailWithTemplate(to, username, activationCode, "Your Password Recovery", "password-recovery-email-template");
    }

    private void sendEmailWithTemplate(String to, String username, String code, String subject, String templateName) throws MessagingException {
        String content = buildEmailContent(templateName, username, code);
        sendEmail(to, subject, content);
    }

    private String buildEmailContent(String templateName, String username, String code) {
        Context context = new Context();
        context.setVariable("username", username);
        context.setVariable("fiveDigitCode", code);
        return templateEngine.process(templateName, context);
    }

    private void sendEmail(String to, String subject, String content) throws MessagingException {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(content, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new EmailFailedToSentException();
        }
    }



}
