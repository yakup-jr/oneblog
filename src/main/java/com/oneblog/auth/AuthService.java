package com.oneblog.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.oneblog.auth.dto.AuthenticationResponseDto;
import com.oneblog.auth.dto.LoginRequestDto;
import com.oneblog.auth.dto.RegistrationEmailVerification;
import com.oneblog.auth.dto.RegistrationRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface AuthService {

	String register(RegistrationRequestDto request);

	String verifyEmail(RegistrationEmailVerification request);

	AuthenticationResponseDto authenticate(LoginRequestDto request);

	AuthenticationResponseDto refreshToken(HttpServletRequest request, HttpServletResponse response)
		throws NoSuchMethodException, MissingRequestHeaderException, GeneralSecurityException;

	AuthenticationResponseDto signUpWithGoogle(GoogleIdToken.Payload payload)
		throws GeneralSecurityException, IOException;

	AuthenticationResponseDto loginWithGoogle(String token) throws GeneralSecurityException, IOException;

}
