package com.nadi_astrology_backend.nadi_astrology_backend.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;


    // =====================================================
    // SEND HTML EMAIL
    // =====================================================

    public void sendHtmlEmail(
            String to,
            String subject,
            String htmlContent
    ) {

        try {

            MimeMessage message =
                    mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            message,
                            true,
                            "UTF-8"
                    );

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(
                    htmlContent,
                    true
            );

            mailSender.send(message);

        } catch (MessagingException e) {

            throw new IllegalStateException(
                    "Failed to send email to: " + to,
                    e
            );
        }
    }


    // =====================================================
    // SIMPLE TEXT EMAIL
    // =====================================================

    public void sendTextEmail(
            String to,
            String subject,
            String text
    ) {

        try {

            MimeMessage message =
                    mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            message,
                            false,
                            "UTF-8"
                    );

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(
                    text,
                    false
            );

            mailSender.send(message);

        } catch (MessagingException e) {

            throw new IllegalStateException(
                    "Failed to send email to: " + to,
                    e
            );
        }
    }
}