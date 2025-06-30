package com.oneblog.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oneblog.auth.dto.AuthenticationResponseDto;
import com.oneblog.auth.dto.LoginRequestDto;
import com.oneblog.auth.dto.RegistrationEmailVerification;
import com.oneblog.auth.dto.RegistrationRequestDto;
import com.oneblog.auth.exception.InvalidVerificationCodeException;
import com.oneblog.exceptions.ApiRequestException;
import com.oneblog.helpers.IntegrationTest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;

import java.security.GeneralSecurityException;
import java.security.SignatureException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
public class AuthControllerMockTest {
	private static final String VALID_TOKEN = "valid_token";
	private static final String INVALID_TOKEN = "invalid_token";
	private static final String BEARER_TOKEN =
		"Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.KMUFsIDTnFmyG3nMiGM6H9FNFUROf3wh7SmqJp-QV30";

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private AuthService authService;

	@Test
	void register_UsernameAlreadyExists_Return400() throws Exception {
		RegistrationRequestDto request = new RegistrationRequestDto("usernameExists", "newUser@mail.com", "strongPass");

		when(authService.register(request)).thenThrow(new ApiRequestException("Username is already taken"));

		mockMvc.perform(post("/registration").contentType(MediaType.APPLICATION_JSON)
		                                     .content(new ObjectMapper().writeValueAsString(request)))
		       .andExpect(status().isBadRequest());

		verify(authService, times(1)).register(request);
	}

	@Test
	void register_EmailAlreadyExists_Return400() throws Exception {
		RegistrationRequestDto request = new RegistrationRequestDto("newUser", "emailExists@mail.com", "strongPass");

		when(authService.register(request)).thenThrow(new ApiRequestException("Email is already taken"));

		mockMvc.perform(post("/registration").contentType(MediaType.APPLICATION_JSON)
		                                     .content(new ObjectMapper().writeValueAsString(request)))
		       .andExpect(status().isBadRequest());

		verify(authService, times(1)).register(request);
	}

	@Test
	void register_Success_Return201() throws Exception {
		RegistrationRequestDto request = new RegistrationRequestDto("newUser", "newUser@mail.com", "strongPass");

		when(authService.register(request)).thenReturn("Success registration");

		mockMvc.perform(post("/registration").contentType(MediaType.APPLICATION_JSON)
		                                     .content(new ObjectMapper().writeValueAsString(request)))
		       .andExpect(status().isCreated());

		verify(authService, times(1)).register(request);
	}

	@Test
	void verifyEmail_VerificationCodeInvalid_Return401() throws Exception {
		RegistrationEmailVerification request = new RegistrationEmailVerification("newUser@mail.com", "000000");

		when(authService.verifyEmail(any(RegistrationEmailVerification.class)))
			.thenThrow(new InvalidVerificationCodeException("Code is invalid"));

		mockMvc.perform(post("/registration/email/verify").contentType(MediaType.APPLICATION_JSON)
		                                                  .content(new ObjectMapper().writeValueAsString(request)))
		       .andExpect(status().isUnauthorized());

		verify(authService, times(1)).verifyEmail(any(RegistrationEmailVerification.class));
	}

	@Test
	void verifyEmail_Success_Return200() throws Exception {
		RegistrationEmailVerification request = new RegistrationEmailVerification("newUser@mail.com", "111111");

		when(authService.verifyEmail(request)).thenReturn("Success email verification");

		mockMvc.perform(post("/registration/email/verify").contentType(MediaType.APPLICATION_JSON)
		                                                  .content(new ObjectMapper().writeValueAsString(request)))
		       .andExpect(status().isOk());
	}

	@Test
	void login_InvalidUsername_Return401() throws Exception {
		LoginRequestDto request = new LoginRequestDto("invalidUsername", "password");

		when(authService.authenticate(any(LoginRequestDto.class))).thenThrow(
			new BadCredentialsException("Username not found"));

		mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON)
		                              .content(new ObjectMapper().writeValueAsString(request)))
		       .andExpect(status().isUnauthorized());

		verify(authService, times(1)).authenticate(any(LoginRequestDto.class));
	}

	@Test
	void login_InvalidPassword_Return401() throws Exception {
		LoginRequestDto request = new LoginRequestDto("username", "invalidPassword");

		when(authService.authenticate(any(LoginRequestDto.class))).thenThrow(
			new BadCredentialsException("Password is invalid"));

		mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON)
		                              .content(new ObjectMapper().writeValueAsString(request)))
		       .andExpect(status().isUnauthorized());

		verify(authService, times(1)).authenticate(any(LoginRequestDto.class));
	}

	@Test
	void login_Success_Return200() throws Exception {
		LoginRequestDto request = new LoginRequestDto("username", "password");

		when(authService.authenticate(any(LoginRequestDto.class))).thenReturn(
			new AuthenticationResponseDto("access_token", "refresh_token"));

		mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON)
		                              .content(new ObjectMapper().writeValueAsString(request)))
		       .andExpect(status().isOk());

		verify(authService, times(1)).authenticate(any(LoginRequestDto.class));
	}

	@Test
	void refreshToken_SignatureInvalid_Return401() throws Exception {
		when(authService.refreshToken(any(HttpServletRequest.class), any(HttpServletResponse.class))).thenThrow(
			new SignatureException());

		mockMvc.perform(post("/refresh-token").contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION,
		                                                                                      BEARER_TOKEN))
		       .andExpect(status().isUnauthorized());
	}

	@Test
	void refreshToken_ExpiredToken_Return401() throws Exception {
		when(authService.refreshToken(any(HttpServletRequest.class), any(HttpServletResponse.class))).thenThrow(
			new SignatureException());

		mockMvc.perform(post("/refresh-token").contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION,
		                                                                                      BEARER_TOKEN))
		       .andExpect(status().isUnauthorized());
	}

	@Test
	void refreshToken_Success_Return200() throws Exception {
		when(authService.refreshToken(any(HttpServletRequest.class), any(HttpServletResponse.class))).thenReturn(
			new AuthenticationResponseDto("accessToken", "refreshToken"));

		mockMvc.perform(post("/refresh-token").contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION,
		                                                                                      BEARER_TOKEN))
		       .andExpect(status().isOk());
	}

	@Test
	void authenticationOauth_InvalidToken_Return401() throws Exception {
		when(authService.loginWithGoogle(INVALID_TOKEN))
			.thenThrow(new GeneralSecurityException("Invalid token"));

		mockMvc.perform(post("/login/oauth2/code/google")
			                .contentType(MediaType.APPLICATION_JSON)
			                .content(INVALID_TOKEN))
		       .andExpect(status().isUnauthorized());

		verify(authService, times(1)).loginWithGoogle(INVALID_TOKEN);
	}

	@Test
	void authenticationOauth_Success_Return200() throws Exception {
		AuthenticationResponseDto response = new AuthenticationResponseDto("access_token", "refresh_token");

		when(authService.loginWithGoogle(VALID_TOKEN))
			.thenReturn(response);

		mockMvc.perform(post("/login/oauth2/code/google")
			                .contentType(MediaType.APPLICATION_JSON)
			                .content(VALID_TOKEN))
		       .andExpect(status().isOk());

		verify(authService, times(1)).loginWithGoogle(VALID_TOKEN);
	}
}
