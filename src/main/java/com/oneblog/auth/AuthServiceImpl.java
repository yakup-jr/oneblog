package com.oneblog.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.oneblog.auth.dto.AuthenticationResponseDto;
import com.oneblog.auth.dto.LoginRequestDto;
import com.oneblog.auth.dto.RegistrationRequestDto;
import com.oneblog.auth.jwt.JwtService;
import com.oneblog.user.User;
import com.oneblog.user.UserNotFoundException;
import com.oneblog.user.UserRepository;
import com.oneblog.user.UserService;
import com.oneblog.user.role.RoleName;
import com.oneblog.user.role.RoleRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthServiceImpl implements AuthService {

	@Value("${OAUTH_GOOGLE_CLIENT_ID}")
	private String clientId;

	private final JwtService jwtService;

	private final PasswordEncoder passwordEncoder;

	private final AuthenticationManager authenticationManager;

	private final TokenRepository tokenRepository;

	private final UserRepository userRepository;

	private final UserService userService;

	private final RoleRepository roleRepository;

	private final GoogleIdTokenVerifier
		verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
			           .setAudience(Collections.singletonList(clientId))
			           .build();


	public AuthServiceImpl(
		JwtService jwtService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,
		TokenRepository tokenRepository, UserRepository userRepository, UserService userService,
		RoleRepository roleRepository) {
		this.jwtService = jwtService;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.tokenRepository = tokenRepository;
		this.userRepository = userRepository;
		this.userService = userService;
		this.roleRepository = roleRepository;
	}


	@Override
	public void register(RegistrationRequestDto request) {
		User user = new User();

		user.setNickname(request.getUsername());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRoles(List.of(roleRepository.findByName(RoleName.ROLE_USER).get()));

		userRepository.save(user);
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
	public ResponseEntity<AuthenticationResponseDto> refreshToken(
		HttpServletRequest request, HttpServletResponse response) {

		String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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

			return ResponseEntity.status(HttpStatus.OK).body(new AuthenticationResponseDto(accessToken, refreshToken));

		}

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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
		GoogleIdToken.Payload payload = verifyGoogleToken(token);
		boolean emailVerified = payload.getEmailVerified();
		if (!emailVerified) {
			throw new GeneralSecurityException("email verification failed");
		}

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

	private GoogleIdToken.Payload verifyGoogleToken(String token) throws GeneralSecurityException, IOException {
		String tokenToVerify = token.split("\"")[3];
		GoogleIdToken idToken = verifier.verify(tokenToVerify);
		if (idToken != null) {
			return idToken.getPayload();
		}
		throw new GeneralSecurityException("token verification failed");
	}
}
