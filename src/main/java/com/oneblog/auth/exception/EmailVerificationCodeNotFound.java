package com.oneblog.auth.exception;

public class EmailVerificationCodeNotFound extends RuntimeException {
	public EmailVerificationCodeNotFound(String message) {
		super(message);
	}
}
