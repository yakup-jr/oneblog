package com.oneblog.auth.exception;

public class EmailVerificationCodeAlreadySendException extends RuntimeException {
	public EmailVerificationCodeAlreadySendException(String message) {
		super(message);
	}

}
