package com.oneblog.auth.exception;

public class InvalidVerificationCodeException extends RuntimeException {
	public InvalidVerificationCodeException(String message) {
		super(message);
	}
}
