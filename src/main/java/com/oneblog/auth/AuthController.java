package com.oneblog.auth;

import com.oneblog.auth.dto.AuthenticationResponseDto;
import com.oneblog.auth.dto.LoginRequestDto;
import com.oneblog.auth.dto.RegistrationRequestDto;
import com.oneblog.exceptions.ApiRequestException;
import com.oneblog.user.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping
public class AuthController {

	private final AuthService authService;

	private final UserService userService;

	public AuthController(AuthService authService, UserService userService) {
		this.authService = authService;
		this.userService = userService;
	}

	@PostMapping("/registration")
	public ResponseEntity<String> register(@RequestBody RegistrationRequestDto registrationRequestDto) {
		if (userService.existsByNickname(registrationRequestDto.getUsername())) {
			throw new ApiRequestException("Username is already taken");
		}

		if (userService.existsByEmail(registrationRequestDto.getEmail())) {
			throw new ApiRequestException("Email is already taken");
		}

		authService.register(registrationRequestDto);

		return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
	}

	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponseDto> login(@RequestBody LoginRequestDto request) {
		return ResponseEntity.status(HttpStatus.OK).body(authService.authenticate(request));
	}

	@PostMapping("/refresh-token")
	public ResponseEntity<AuthenticationResponseDto> refreshToken(
		HttpServletRequest request, HttpServletResponse response) {
		try {
			return authService.refreshToken(request, response);
		} catch (SignatureException | ExpiredJwtException e) {
			throw new SignatureException(e.getMessage());
		}
	}

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
