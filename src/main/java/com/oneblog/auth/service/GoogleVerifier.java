package com.oneblog.auth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * The interface Google verifier.
 */
public interface GoogleVerifier {

	/**
	 * Verify google id token . payload.
	 *
	 * @param token the token
	 * @return the google id token . payload
	 * @throws GeneralSecurityException the general security exception
	 * @throws IOException              the io exception
	 */
	GoogleIdToken.Payload verify(String token) throws GeneralSecurityException, IOException;

}
