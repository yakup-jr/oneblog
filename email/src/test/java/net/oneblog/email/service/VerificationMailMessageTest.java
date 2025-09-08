package net.oneblog.email.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VerificationMailMessageTest {

    @InjectMocks
    private VerificationMailMessage verificationMailMessage;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private CodeGenerator codeGenerator;

    @Test
    void sendVerificationCode_Success() {
        String email = "test@example.com";
        String expectedCode = "123456";
        when(codeGenerator.generateSixDigits()).thenReturn(expectedCode);

        String code = verificationMailMessage.sendVerificationCode(email);

        assertEquals(expectedCode, code);
        verify(mailSender).send(any(SimpleMailMessage.class));
        verify(codeGenerator).generateSixDigits();
    }

    @Test
    void sendVerificationCode_GeneratesDifferentCodes() {
        String email = "test@example.com";
        when(codeGenerator.generateSixDigits()).thenReturn("123456").thenReturn("654321");

        String code1 = verificationMailMessage.sendVerificationCode(email);
        String code2 = verificationMailMessage.sendVerificationCode(email);

        assertNotEquals(code1, code2, "Generated codes should be different");
    }
}
