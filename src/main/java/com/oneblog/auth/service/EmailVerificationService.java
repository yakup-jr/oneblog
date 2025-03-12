package com.oneblog.auth.service;

import com.oneblog.auth.dto.RegistrationEmailVerification;

public interface EmailVerificationService {

	void sendVerificationCode(String email);

	boolean verifyCode(RegistrationEmailVerification verification);

}
