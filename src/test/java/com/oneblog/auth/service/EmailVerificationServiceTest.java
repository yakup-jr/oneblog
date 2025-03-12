package com.oneblog.auth.service;

import com.oneblog.auth.dto.RegistrationEmailVerification;
import com.oneblog.auth.entity.EmailVerification;
import com.oneblog.auth.exception.EmailVerificationCodeAlreadySendException;
import com.oneblog.auth.repository.EmailVerificationRepository;
import com.oneblog.user.User;
import com.oneblog.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmailVerificationServiceTest {

	@InjectMocks
	private EmailVerificationServiceImpl emailVerificationService;

	@Mock
	private EmailVerificationRepository emailVerificationRepository;

	@Mock
	private UserService userService;

	@Mock
	private VerificationMailMessage verificationMailMessage;

	private final String email = "mail@mail.com";

	@Test
	void sendVerificationCode_VerificationCodeNotExpired_ThrowException() {
		when(emailVerificationRepository.existsByEmailAndCodeNotExpired(email)).thenReturn(Boolean.TRUE);


		assertThatThrownBy(() -> emailVerificationService.sendVerificationCode(email)).isInstanceOf(
			EmailVerificationCodeAlreadySendException.class);
	}

	@Test
	void sendVerificationCode_VerificationCodeExpired_AllWork() {
		when(emailVerificationRepository.existsByEmailAndCodeNotExpired(email)).thenReturn(Boolean.FALSE);
		when(verificationMailMessage.sendVerificationCode(email)).thenReturn("123456");
		when(userService.findByEmail(email)).thenReturn(User.builder().email(email).build());

		emailVerificationService.sendVerificationCode(email);
	}

	@Test
	void verifyCode_VerificationCodeCorrect_ReturnTrue() {
		when(emailVerificationRepository.findByEmail(email)).thenReturn(
			Optional.of(EmailVerification.builder().code("111111").expiresAt(
				LocalDateTime.now().plusMinutes(3)).build()));

		boolean verified = emailVerificationService.verifyCode(new RegistrationEmailVerification(email, "111111"));

		assertThat(verified).isTrue();
	}

	@Test
	void verifyCode_VerificationCodeNotSame_ReturnFalse() {
		when(emailVerificationRepository.findByEmail(email)).thenReturn(Optional.of(EmailVerification.builder().code(
			"111111").expiresAt(LocalDateTime.now().plusMinutes(3)).build()));

		boolean verified = emailVerificationService.verifyCode(new RegistrationEmailVerification(email, "123456"));

		assertThat(verified).isFalse();
	}

	@Test
	void verifyCode_VerificationCodeExpired_ReturnFalse() {
		when(emailVerificationRepository.findByEmail(email)).thenReturn(Optional.of(EmailVerification.builder().code(
			"111111").expiresAt(LocalDateTime.now().minusMinutes(3)).build()));

		boolean verified = emailVerificationService.verifyCode(new RegistrationEmailVerification(email, "111111"));

		assertThat(verified).isFalse();
	}

	@Test
	void verifyCode_VerificationCodeNotExist_ThrowException() {
		when(emailVerificationRepository.findByEmail(email)).thenReturn(Optional.empty());

		assertThatThrownBy(
			() -> emailVerificationService.verifyCode(new RegistrationEmailVerification(email, "111111")));
	}
}