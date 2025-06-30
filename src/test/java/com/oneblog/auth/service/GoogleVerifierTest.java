package com.oneblog.auth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GoogleVerifierTest {

	@InjectMocks
	private GoogleVerifierImpl verifierImpl;

	@Mock
	private GoogleIdTokenVerifier verifier;

	@Mock
	private GoogleIdToken idToken;

	@Mock
	private GoogleIdToken.Payload payload;

	@Test
	void verify_TokenInvalid_ThrowGeneralSecurityException() {
		String token = "invalidToken";

		assertThatThrownBy(() -> verifierImpl.verify(token)).withFailMessage("token not valid")
		                                                    .isInstanceOf(GeneralSecurityException.class);
	}

	@Test
	void verify_TokenVerificationFails_ThrowGeneralSecurityException() throws GeneralSecurityException, IOException {
		String token = "\"token\": \"token\"";

		when(verifier.verify("token")).thenReturn(null);

		assertThatThrownBy(() -> verifierImpl.verify(token)).hasMessage("token verification failed")
		                                                    .isInstanceOf(GeneralSecurityException.class);
	}

	@Test
	void verify_EmailNotVerified_ThrowGeneralSecurityException() throws GeneralSecurityException, IOException {
		String token = "\"token\": \"token\"";

		when(verifier.verify("token")).thenReturn(idToken);
		when(idToken.getPayload()).thenReturn(payload);
		when(payload.getEmailVerified()).thenReturn(false);

		assertThatThrownBy(() -> verifierImpl.verify(token)).hasMessage("email verification failed").isInstanceOf(
			GeneralSecurityException.class);
	}

	@Test
	void verify_Success_ReturnGoogleIdPayload() throws GeneralSecurityException, IOException {
		String token = "\"token\": \"token\"";

		when(verifier.verify("token")).thenReturn(idToken);
		when(idToken.getPayload()).thenReturn(payload);
		when(payload.getEmailVerified()).thenReturn(true);

		GoogleIdToken.Payload result = verifierImpl.verify(token);

		assertThat(result).isNotNull().isInstanceOf(GoogleIdToken.Payload.class);
		assertThat(result.getEmailVerified()).isTrue();
	}
}