package com.oneblog.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.oneblog.auth.dto.AuthenticationResponseDto;
import com.oneblog.auth.dto.LoginRequestDto;
import com.oneblog.auth.dto.RegistrationEmailVerification;
import com.oneblog.auth.dto.RegistrationRequestDto;
import com.oneblog.auth.exception.InvalidVerificationCodeException;
import com.oneblog.auth.jwt.JwtService;
import com.oneblog.auth.repository.TokenRepository;
import com.oneblog.auth.service.EmailVerificationService;
import com.oneblog.auth.service.GoogleVerifier;
import com.oneblog.user.User;
import com.oneblog.user.UserNotFoundException;
import com.oneblog.user.UserRepository;
import com.oneblog.user.UserService;
import com.oneblog.user.role.RoleName;
import com.oneblog.user.role.RoleRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MissingRequestHeaderException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthServiceImpl implements AuthService {

	private final JwtService jwtService;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final TokenRepository tokenRepository;
	private final UserRepository userRepository;
	private final UserService userService;
	private final RoleRepository roleRepository;
	private final EmailVerificationService emailVerificationService;
	private final GoogleVerifier googleVerifier;

	public AuthServiceImpl(
		JwtService jwtService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,
		TokenRepository tokenRepository, UserRepository userRepository, UserService userService,
		RoleRepository roleRepository, EmailVerificationService emailVerificationService,
		GoogleVerifier googleVerifier) {
		this.jwtService = jwtService;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.tokenRepository = tokenRepository;
		this.userRepository = userRepository;
		this.userService = userService;
		this.roleRepository = roleRepository;
		this.emailVerificationService = emailVerificationService;
		this.googleVerifier = googleVerifier;
	}


	@Override
	public String register(RegistrationRequestDto request) {
		if (!userRepository.existsByEmail(request.getEmail())) {
			User user = new User();

			user.setNickname(request.getUsername());
			user.setEmail(request.getEmail());
			user.setPassword(passwordEncoder.encode(request.getPassword()));
			user.setRoles(List.of(roleRepository.findByName(RoleName.ROLE_USER).get()));
			user.setVerificated(false);

			userRepository.save(user);
		}

		emailVerificationService.sendVerificationCode(request.getEmail());

		return "Success registration";
	}

	@Override
	public String verifyEmail(RegistrationEmailVerification request) {
		boolean verified = emailVerificationService.verifyCode(request);
		if (!verified) {
			throw new InvalidVerificationCodeException("Code is invalid");
		}
		User byEmail = userService.findByEmail(request.getEmail());
		byEmail.setVerificated(true);
		userService.save(byEmail);
		return "Success";
	}


	@Override
	public AuthenticationResponseDto authenticate(LoginRequestDto request) {
		authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

		User user =
			userRepository.findByNickname(request.getUsername()).orElseThrow(() -> new UserNotFoundException(
				"User with username " + request.getUsername() + " not found"));

		String accessToken = jwtService.generateAccessToken(user);
		String refreshToken = jwtService.generateRefreshToken(user);

		revokeAllToken(user);

		saveUserToken(accessToken, refreshToken, user);

		return new AuthenticationResponseDto(accessToken, refreshToken);
	}

	@Override
	public AuthenticationResponseDto refreshToken(
		HttpServletRequest request, HttpServletResponse response)
		throws NoSuchMethodException, MissingRequestHeaderException, GeneralSecurityException {

		String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			throw new MissingRequestHeaderException(
				"Authorization",
				new MethodParameter(
					this.getClass()
					    .getDeclaredMethod("refreshToken", HttpServletRequest.class, HttpServletResponse.class), 0
				)
			);
		}

		String token = authorizationHeader.substring(7);
		String username = jwtService.extractUsername(token);

		User user = userRepository.findByNickname(username).orElseThrow(
			() -> new UsernameNotFoundException("User with username " + username + " not found"));

		if (jwtService.isValidRefresh(token, user)) {

			String accessToken = jwtService.generateAccessToken(user);
			String refreshToken = jwtService.generateRefreshToken(user);

			revokeAllToken(user);

			saveUserToken(accessToken, refreshToken, user);

			return new AuthenticationResponseDto(accessToken, refreshToken);
		}
		throw new GeneralSecurityException("Invalid token");
	}

	@Override
	public AuthenticationResponseDto signUpWithGoogle(GoogleIdToken.Payload payload)
		throws GeneralSecurityException, IOException {
		String nickname = String.valueOf(payload.get("given_name"));
		if (userRepository.findByNickname(String.valueOf(payload.get("given_name"))).isPresent()) {
			nickname = nickname.concat(String.valueOf(new Random().nextInt(10000)));
		}
		User savedUser = userService.save(
			User.builder().email(String.valueOf(payload.get("email"))).googleUserId(payload.getSubject()).name(
				String.valueOf(payload.get("name"))).nickname(nickname).build());

		String accessToken = jwtService.generateAccessToken(savedUser);
		String refreshToken = jwtService.generateRefreshToken(savedUser);

		revokeAllToken(savedUser);
		saveUserToken(accessToken, refreshToken, savedUser);

		return new AuthenticationResponseDto(accessToken, refreshToken);
	}

	@Override
	public AuthenticationResponseDto loginWithGoogle(String token) throws GeneralSecurityException, IOException {
		GoogleIdToken.Payload payload = googleVerifier.verify(token);

		String email = String.valueOf(payload.get("email"));
		String googleUserId = String.valueOf(payload.getSubject());

		Optional<User> byGoogleUserId = userRepository.findByGoogleUserId(googleUserId);
		if (byGoogleUserId.isPresent()) {
			User user = byGoogleUserId.get();

			String accessToken = jwtService.generateAccessToken(user);
			String refreshToken = jwtService.generateRefreshToken(user);

			revokeAllToken(user);
			saveUserToken(accessToken, refreshToken, user);

			return new AuthenticationResponseDto(accessToken, refreshToken);
		}
		Optional<User> byEmail = userRepository.findByEmail(email);
		if (byEmail.isPresent()) {
			User user = byEmail.get();
			user.setGoogleUserId(googleUserId);
			User savedUser = userService.save(user);

			String accessToken = jwtService.generateAccessToken(savedUser);
			String refreshToken = jwtService.generateRefreshToken(savedUser);

			revokeAllToken(savedUser);
			saveUserToken(accessToken, refreshToken, savedUser);

			return new AuthenticationResponseDto(accessToken, refreshToken);
		}
		return signUpWithGoogle(payload);
	}

	private void revokeAllToken(User user) {
		List<Token> validToken = tokenRepository.findAllAccessTokenByUser(user.getUserId());

		if (!validToken.isEmpty()) {
			validToken.forEach(token -> {
				token.setLoggedOut(true);
			});
		}

		tokenRepository.saveAll(validToken);
	}

	private void saveUserToken(String accessToken, String refreshToken, User user) {
		Token token = new Token();

		token.setAccessToken(accessToken);
		token.setRefreshToken(refreshToken);
		token.setUser(user);
		token.setLoggedOut(false);

		tokenRepository.save(token);
	}
}
