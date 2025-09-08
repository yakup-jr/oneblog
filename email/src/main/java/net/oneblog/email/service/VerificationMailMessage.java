package net.oneblog.email.service;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type Verification mail message.
 */
@Component
@AllArgsConstructor
public class VerificationMailMessage {

    private final CodeGenerator codeGenerator;
    private final JavaMailSender mailSender;

    /**
     * Send verification code string.
     *
     * @param email the email
     * @return the string
     */
    @Transactional
    public String sendVerificationCode(String email) {
        String code = codeGenerator.generateSixDigits();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("oneblog.space@resend.dev");
        message.setSubject("Verification Code");
        message.setText("Verification code for registration on oneblog.space: " + code);
        message.setTo(email);
        mailSender.send(message);

        return code;
    }

}
