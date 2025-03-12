package com.oneblog.auth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Component
public class GoogleVerifierImpl implements GoogleVerifier {

	private final GoogleIdTokenVerifier verifier;

	public GoogleVerifierImpl(GoogleIdTokenVerifier verifier) {this.verifier = verifier;}

	@Override
	public GoogleIdToken.Payload verify(String token) throws GeneralSecurityException, IOException {
		GoogleIdToken idToken = verifier.verify(getTokenToVerify(token));
		if (idToken == null) {
			throw new GeneralSecurityException("token verification failed");
		}
		if (idToken.getPayload() == null || !idToken.getPayload().getEmailVerified()) {
			throw new GeneralSecurityException("email verification failed");
		}
		return idToken.getPayload();
	}


	private String getTokenToVerify(String token) throws GeneralSecurityException {
		try {
			return token.split("\"")[3];
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new GeneralSecurityException("token not valid");
		}
	}
}
