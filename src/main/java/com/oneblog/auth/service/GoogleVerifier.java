package com.oneblog.auth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface GoogleVerifier {

	GoogleIdToken.Payload verify(String token) throws GeneralSecurityException, IOException;

}
