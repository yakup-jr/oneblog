package com.oneblog.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.oneblog.auth.dto.AuthenticationResponseDto;
import com.oneblog.auth.dto.LoginRequestDto;
import com.oneblog.auth.dto.RegistrationRequestDto;
import com.oneblog.helpers.IntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@IntegrationTest
public class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private AuthService authService;

	private GreenMail greenMail;

	private static final ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	public void setUp() {
		greenMail = new GreenMail(ServerSetupTest.SMTP);
		greenMail.start();
	}

	@AfterEach
	public void tearDown() {
		greenMail.stop();
	}

	@Test
	void register_Return400_UsernameAlreadyExists() throws Exception {
	    RegistrationRequestDto request = new RegistrationRequestDto("hunter", "hunter@mail.com", "test");

	    mockMvc.perform(post("/registration")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(request)))
	            .andExpect(status().isBadRequest());
	}

	@Test
	void register_Return400_EmailAlreadyExists() throws Exception {
	    RegistrationRequestDto request = new RegistrationRequestDto("hunter1", "hunter@mail.com", "test");

	    mockMvc.perform(post("/registration")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(request)))
	            .andExpect(status().isBadRequest());
	}

	@Test
	void register_Return201_Created() throws Exception {
	    RegistrationRequestDto request = new RegistrationRequestDto("newUsername", "newMail@mail.com", "test");

	    mockMvc.perform(post("/registration")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(request)))
	            .andExpect(status().isCreated());
	}

	@Test
	void login_Return401_UsernameInvalid() throws Exception {
	    LoginRequestDto request = new LoginRequestDto("invalidUsername", "strongPass1");

	    mockMvc.perform(post("/login")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(request)))
	            .andExpect(status().isUnauthorized());
	}

	@Test
	void login_Return401_PasswordInvalid() throws Exception {
	    LoginRequestDto request = new LoginRequestDto("hunter", "invalidPass");

	    mockMvc.perform(post("/login")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(request)))
	            .andExpect(status().isUnauthorized());
	}

	@Test
	void login_Return200_Ok() throws Exception {
	    LoginRequestDto request = new LoginRequestDto("hunter", "strongPass1");

	    mockMvc.perform(post("/login")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(request)))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.accessToken", notNullValue()))
	            .andExpect(jsonPath("$.refreshToken", notNullValue()));
	}

	@Test
	void refreshToken_Return401_HeaderAuthorizationNotPresent() throws Exception {
	    mockMvc.perform(post("/refresh-token")
	            .contentType(MediaType.APPLICATION_JSON))
	            .andExpect(status().isUnauthorized());
	}

	@Test
	void refreshToken_Return401_HeaderAuthorizationEmpty() throws Exception {
	    mockMvc.perform(post("/refresh-token")
	            .header("Authorization", "")
	            .contentType(MediaType.APPLICATION_JSON))
	            .andExpect(status().isUnauthorized());
	}

	@Test
	void refreshToken_Return401_SignInvalid() throws Exception {
	    mockMvc.perform(post("/refresh-token")
	            .contentType(MediaType.APPLICATION_JSON)
	            .header("Authorization", "Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ5YWt1cF9qciIsImlhdCI6MTczODIzMDk2MiwiZXhwI.jsfasfuhi23haFDSFg23_12"))
	            .andExpect(status().isUnauthorized());
	}

	@Test
	void refreshToken_Return401_TokenExpired() throws Exception {
	    mockMvc.perform(post("/refresh-token")
	            .contentType(MediaType.APPLICATION_JSON)
	            .header("Authorization", "Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ5YWt1cF9qciIsImlhdCI6MTczODIzMDk2MiwiZXhwIjoxNzM4MjY2OTYyfQ.bEpB6h2R0560lx-E9VPdeKYtfkoIUUebvOUWqr5W0irJDvlmLmka__dtWrQUPeKT"))
	            .andExpect(status().isUnauthorized());
	}

	@Test
	@Disabled
	void refreshToken_Return200_Ok() throws Exception {
	    AuthenticationResponseDto responseDto = authService.authenticate(new LoginRequestDto("hunter", "strongPass1"));

	    mockMvc.perform(post("/refresh-token")
	            .contentType(MediaType.APPLICATION_JSON)
	            .header("Authorization", "Bearer " + responseDto.refreshToken()))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.accessToken", notNullValue()))
	            .andExpect(jsonPath("$.refreshToken", notNullValue()));
	}

	@Test
	void authenticationOauth_Return400_BodyEmpty() throws Exception {
	    mockMvc.perform(post("/login/oauth2/code/google")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(""))
	            .andExpect(status().isBadRequest());
	}

	@Test
	void authenticationOauth_Return401_TokenInvalid() throws Exception {
	    String invalidToken = "invalid_token";

	    mockMvc.perform(post("/login/oauth2/code/google")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(invalidToken))
	            .andExpect(status().isUnauthorized());
	}

	@Test
	@Disabled
	void authenticationOauth_Success_Return200() throws Exception {
	    String validToken = "valid_token";
	    AuthenticationResponseDto response = authService.loginWithGoogle(validToken);

	    mockMvc.perform(post("/login/oauth2/code/google")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(validToken))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.accessToken").value(response.accessToken()))
	            .andExpect(jsonPath("$.refreshToken").value(response.refreshToken()));
	}
}
