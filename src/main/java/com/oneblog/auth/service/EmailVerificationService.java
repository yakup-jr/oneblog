package com.oneblog.auth.service;

import com.oneblog.auth.dto.RegistrationEmailVerification;

/**
 * The interface Email verification service.
 */
public interface EmailVerificationService {

	/**
	 * Send verification code.
	 *
	 * @param email the email
	 */
	void sendVerificationCode(String email);

	/**
	 * Verify code boolean.
	 *
	 * @param verification the verification
	 * @return the boolean
	 */
	boolean verifyCode(RegistrationEmailVerification verification);

}
