package com.oneblog.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface AuthService {

	void register(RegistrationRequestDto request);

	AuthenticationResponseDto authenticate(LoginRequestDto request);

	ResponseEntity<AuthenticationResponseDto> refreshToken(HttpServletRequest request, HttpServletResponse response);

	AuthenticationResponseDto signUpWithGoogle(GoogleIdToken.Payload payload)
		throws GeneralSecurityException, IOException;

	AuthenticationResponseDto loginWithGoogle(String token) throws GeneralSecurityException, IOException;

}
