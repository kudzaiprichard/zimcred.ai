package com.intela.zimcredai.Services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendPasswordResetEmail(String to, String name, String resetLink) throws MessagingException, IOException, IOException {
        String template = new String(Files.readAllBytes(Paths.get("src/main/resources/templates/reset-password-template.html")));

        String htmlContent = template.replace("{{name}}", name)
                .replace("{{resetLink}}", resetLink)
                .replace("{{year}}", String.valueOf(java.time.Year.now().getValue()));

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject("Password Reset Request");
        helper.setText(htmlContent, true);
        mailSender.send(message);
    }
}
