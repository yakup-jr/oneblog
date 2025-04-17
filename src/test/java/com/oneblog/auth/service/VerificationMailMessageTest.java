package com.oneblog.auth.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VerificationMailMessageTest {

	@InjectMocks
	private VerificationMailMessage verificationMailMessage;

	@Mock
	private static JavaMailSender mailSender;

	@Mock
	private CodeGenerator codeGenerator;

	@Test
	void sendVerificationCode_ReturnVerificationCode() {
		when(codeGenerator.generateSixDigits()).thenReturn("123456");

		String code = verificationMailMessage.sendVerificationCode("somemail@mail.com");

		assertThat(code).isInstanceOf(String.class);
		assertThat(code.length()).isEqualTo(6);
		assertThat(code).isEqualTo("123456");
	}

}
