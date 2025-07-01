package com.oneblog.auth;

import com.oneblog.auth.dto.AuthenticationResponseDto;
import com.oneblog.auth.dto.LoginRequestDto;
import com.oneblog.auth.dto.RegistrationEmailVerification;
import com.oneblog.auth.dto.RegistrationRequestDto;
import com.oneblog.exceptions.ApiRequestException;
import com.oneblog.user.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * The type Auth controller.
 */
@RestController
@RequestMapping
public class AuthController {

	private final AuthService authService;

	private final UserService userService;

	/**
	 * Instantiates a new Auth controller.
	 *
	 * @param authService the auth service
	 * @param userService the user service
	 */
	public AuthController(AuthService authService, UserService userService) {
		this.authService = authService;
		this.userService = userService;
	}

	/**
	 * Register response entity.
	 *
	 * @param registrationRequestDto the registration request dto
	 * @return the response entity
	 */
	@PostMapping("/registration")
	public ResponseEntity<String> register(@RequestBody RegistrationRequestDto registrationRequestDto) {
		if (userService.existsByNickname(registrationRequestDto.getUsername())) {
			throw new ApiRequestException("Username is already taken");
		}

		if (userService.existsByEmail(registrationRequestDto.getEmail())) {
			throw new ApiRequestException("Email is already taken");
		}

		String result = authService.register(registrationRequestDto);

		return ResponseEntity.status(HttpStatus.CREATED).body(result);
	}

	/**
	 * Verify email response entity.
	 *
	 * @param emailVerification the email verification
	 * @return the response entity
	 */
	@PostMapping("/registration/email/verify")
	public ResponseEntity<String> verifyEmail(@RequestBody RegistrationEmailVerification emailVerification) {
		String result = authService.verifyEmail(emailVerification);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}

	/**
	 * Login response entity.
	 *
	 * @param request the request
	 * @return the response entity
	 */
	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponseDto> login(@RequestBody LoginRequestDto request) {
		return ResponseEntity.status(HttpStatus.OK).body(authService.authenticate(request));
	}

	/**
	 * Refresh token response entity.
	 *
	 * @param request  the request
	 * @param response the response
	 * @return the response entity
	 */
	@PostMapping("/refresh-token")
	public ResponseEntity<AuthenticationResponseDto> refreshToken(
		HttpServletRequest request, HttpServletResponse response) {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(authService.refreshToken(request, response));
		} catch (SignatureException | ExpiredJwtException | GeneralSecurityException | MissingRequestHeaderException |
		         NoSuchMethodException e) {
			throw new SignatureException(e.getMessage());
		}
	}

	/**
	 * Authentication oauth response entity.
	 *
	 * @param token the token
	 * @return the response entity
	 * @throws GeneralSecurityException the general security exception
	 */
	@PostMapping("/login/oauth2/code/google")
	public ResponseEntity<AuthenticationResponseDto> authenticationOauth(@RequestBody String token)
		throws GeneralSecurityException {
		try {
			AuthenticationResponseDto authenticationResponseDto = authService.loginWithGoogle(token);
			return ResponseEntity.status(HttpStatus.OK).body(authenticationResponseDto);
		} catch (GeneralSecurityException e) {
			throw new java.security.SignatureException(e);
		} catch (IOException e) {
			throw new ApiRequestException(e.getMessage());
		}
	}
}
