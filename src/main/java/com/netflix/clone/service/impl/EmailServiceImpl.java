package com.netflix.clone.service.impl;

import com.netflix.clone.exception.EmailNotVerifiedException;
import com.netflix.clone.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
    private final JavaMailSender mailSender;

    @Value("${app.frontend.url:http://localhost:4200}")
    private String frontendUrl;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendVerificationEmail(String toEmail, String token) {
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Email Verification - Netflix Clone");

            String verificationLink = frontendUrl + "/verify-email?token=" + token;
            String emailBody = "Dear User,\n\n"
                    + "Thank you for registering on Netflix Clone! Please click the link below to verify your email address:\n"
                    + verificationLink + "\n\n"
                    + "If you did not sign up for this account, please ignore this email.\n\n"
                    + "Best regards,\n"
                    + "Netflix Clone Team";

            message.setText(emailBody);
            mailSender.send(message);
            logger.info("Verification email sent to {}", toEmail);
        }catch (Exception e){
            logger.error("Failed to send verification email to {}: {}", toEmail, e.getMessage());
            throw new EmailNotVerifiedException("Failed to send verification email. Please try again later.");
        }
    }
    @Override
    public void sendPasswordResetEmail(String toEmail, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Password Reset - Netflix Clone");

            String resetLink = frontendUrl + "/reset-password?token=" + token;
            String emailBody = "Dear User,\n\n"
                    + "We received a request to reset your password. Please click the link below to reset your password:\n"
                    + resetLink + "\n\n"
                    + "If you did not request a password reset, please ignore this email.\n\n"
                    + "Best regards,\n"
                    + "Netflix Clone Team";

            message.setText(emailBody);
            mailSender.send(message);
            logger.info("Password reset email sent to {}", toEmail);


        }catch (Exception e){
            logger.error("Failed to send password reset email to {}: {}", toEmail, e.getMessage());
            throw new EmailNotVerifiedException("Failed to send password reset email. Please try again later.");
        }

    }
}
