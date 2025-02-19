package com.x.backend.services;

import com.x.backend.exceptions.EmailFailedToSentException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;

@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public static final String SENDER_EMAIL = "support@x.com";
    public static final String SENDER_NAME = "X";

    public MailServiceImpl(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendVerificationCodeViaEmail(String to, String verificationCode) throws EmailFailedToSentException {
        sendEmailWithTemplate(to, verificationCode, "Your Verification Code", "activation-email-template");
    }

    private void sendEmailWithTemplate(String to, String code, String subject, String templateName) throws EmailFailedToSentException {
        String content = buildEmailContent(templateName, code);
        sendEmail(to, subject, content);
    }

    private String buildEmailContent(String templateName, String code) {
        Context context = new Context();
        context.setVariable("verificationCode", code);
        return templateEngine.process(templateName, context);
    }

    private void sendEmail(String to, String subject, String content) throws EmailFailedToSentException {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(new InternetAddress(SENDER_EMAIL, SENDER_NAME));
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);

        } catch (MailException | MessagingException e) {
            throw new EmailFailedToSentException();

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
