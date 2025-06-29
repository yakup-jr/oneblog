package com.oneblog.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.oneblog.auth.dto.AuthenticationResponseDto;
import com.oneblog.auth.dto.LoginRequestDto;
import com.oneblog.auth.dto.RegistrationEmailVerification;
import com.oneblog.auth.dto.RegistrationRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.MissingRequestHeaderException;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * The interface Auth service.
 */
public interface AuthService {

	/**
	 * Register string.
	 *
	 * @param request the request
	 * @return the string
	 */
	String register(RegistrationRequestDto request);

	/**
	 * Verify email string.
	 *
	 * @param request the request
	 * @return the string
	 */
	String verifyEmail(RegistrationEmailVerification request);

	/**
	 * Authenticate authentication response dto.
	 *
	 * @param request the request
	 * @return the authentication response dto
	 */
	AuthenticationResponseDto authenticate(LoginRequestDto request);

	/**
	 * Refresh token authentication response dto.
	 *
	 * @param request  the request
	 * @param response the response
	 * @return the authentication response dto
	 * @throws NoSuchMethodException         the no such method exception
	 * @throws MissingRequestHeaderException the missing request header exception
	 * @throws GeneralSecurityException      the general security exception
	 */
	AuthenticationResponseDto refreshToken(HttpServletRequest request, HttpServletResponse response)
		throws NoSuchMethodException, MissingRequestHeaderException, GeneralSecurityException;

	/**
	 * Sign up with google authentication response dto.
	 *
	 * @param payload the payload
	 * @return the authentication response dto
	 * @throws GeneralSecurityException the general security exception
	 * @throws IOException              the io exception
	 */
	AuthenticationResponseDto signUpWithGoogle(GoogleIdToken.Payload payload)
		throws GeneralSecurityException, IOException;

	/**
	 * Login with google authentication response dto.
	 *
	 * @param token the token
	 * @return the authentication response dto
	 * @throws GeneralSecurityException the general security exception
	 * @throws IOException              the io exception
	 */
	AuthenticationResponseDto loginWithGoogle(String token) throws GeneralSecurityException, IOException;

}
