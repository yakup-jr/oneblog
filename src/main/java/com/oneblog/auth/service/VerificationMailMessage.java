package com.oneblog.auth.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class VerificationMailMessage {

	private final CodeGenerator codeGenerator;

	private final JavaMailSender mailSender;

	public VerificationMailMessage(CodeGenerator codeGenerator, JavaMailSender mailSender) {
		this.codeGenerator = codeGenerator;
		this.mailSender = mailSender;
	}

	public String sendVerificationCode(String email) {
		String code = codeGenerator.generateSixDigits();

		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("oneblog");
		message.setSubject("Verification Code");
		message.setText("Verification code: " + code);
		message.setTo(email);
		mailSender.send(message);

		return code;
	}

}
