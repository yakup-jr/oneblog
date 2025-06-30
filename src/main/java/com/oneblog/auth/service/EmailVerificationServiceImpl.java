package com.oneblog.auth.service;

import com.oneblog.auth.dto.RegistrationEmailVerification;
import com.oneblog.auth.entity.EmailVerification;
import com.oneblog.auth.exception.EmailVerificationCodeAlreadySendException;
import com.oneblog.auth.exception.EmailVerificationCodeNotFound;
import com.oneblog.auth.repository.EmailVerificationRepository;
import com.oneblog.user.User;
import com.oneblog.user.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * The type Email verification service.
 */
@Service
public class EmailVerificationServiceImpl implements EmailVerificationService {

	private final LocalDateTime emailCodeExpiration = LocalDateTime.now().plusMinutes(10);

	private final EmailVerificationRepository emailVerificationRepository;

	private final VerificationMailMessage mailMessage;

	private final UserService userService;

	/**
	 * Instantiates a new Email verification service.
	 *
	 * @param emailVerificationRepository the email verification repository
	 * @param mailMessage                 the mail message
	 * @param userService                 the user service
	 */
	public EmailVerificationServiceImpl(
		EmailVerificationRepository emailVerificationRepository, VerificationMailMessage mailMessage,
		UserService userService) {
		this.emailVerificationRepository = emailVerificationRepository;
		this.mailMessage = mailMessage;
		this.userService = userService;
	}

	@Override
	public void sendVerificationCode(String email) {
		if (emailVerificationRepository.existsByEmailAndCodeNotExpired(email)) {
			throw new EmailVerificationCodeAlreadySendException("Verification code already send");
		}
		String code = mailMessage.sendVerificationCode(email);
		User user = userService.findByEmail(email);
		EmailVerification emailVerification =
			EmailVerification.builder().code(code).user(user).expiresAt(emailCodeExpiration).build();
		emailVerificationRepository.save(emailVerification);
	}

	@Override
	public boolean verifyCode(RegistrationEmailVerification verificationPayload) {
		EmailVerification verification = emailVerificationRepository.findByEmail(verificationPayload.getEmail())
		                                                            .orElseThrow(
			                                                            () -> new EmailVerificationCodeNotFound(
				                                                            "Verification code with email " +
				                                                            verificationPayload.getEmail() +
				                                                            " not found"));
		emailVerificationRepository.delete(verification);
		return verification.getCode().equals(verificationPayload.getVerificationCode()) &&
		       verification.getExpiresAt().isAfter(LocalDateTime.now());
	}
}
