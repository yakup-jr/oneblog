package com.oneblog.auth;

import com.oneblog.auth.dto.AuthenticationResponseDto;
import com.oneblog.auth.dto.LoginRequestDto;
import com.oneblog.config.TestConfig;
import com.oneblog.helpers.DatabaseCleanerExtension;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = {TestConfig.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(value = "/testdb/data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@ExtendWith(DatabaseCleanerExtension.class)
public class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private AuthService authService;

	@Test
	void register_Return400_UsernameAlreadyExists() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/registration").contentType(MediaType.APPLICATION_JSON)
		                                      .content("""
			                                               { "username": "hunter",
			                                                      "email": "hunter@mail.com",
			                                                          "password": "test"
			                                                }"""))
		       .andExpect(status().isBadRequest());
	}

	@Test
	void register_Return400_EmailAlreadyExists() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/registration").contentType(MediaType.APPLICATION_JSON).content("""
			                                                                                                             	{
			                                                                                                             		"username": "hunter1",
			                                                                                                             		"email": "hunter@mail.com",
			                                                                                                             		"password": "test"
			                                                                                                             	}
			                                                                                                             """))
		       .andExpect(status().isBadRequest());
	}

	@Test
	void register_Return201_Created() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/registration").contentType(MediaType.APPLICATION_JSON).content("""
			                                                                                                             	{
			                                                                                                             		"username": "newUsername",
			                                                                                                             		"email": "newMail@mail.com",
			                                                                                                             		"password": "test"
			                                                                                                             	}
			                                                                                                             """))
		       .andExpect(status().isCreated());
	}

	@Test
	void login_Return401_UsernameInvalid() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON).content("""
			                                                                                                      	{
			                                                                                                      		"username": "invalidUsername",
			                                                                                                      		"password": "strongPass1"
			                                                                                                      	}
			                                                                                                      """))
		       .andExpect(status().isUnauthorized());
	}

	@Test
	void login_Return401_PasswordInvalid() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON).content("""
			                                                                                                      	{
			                                                                                                      		"username": "hunter",
			                                                                                                      		"password": "invalidPass"
			                                                                                                      	}
			                                                                                                      """))
		       .andExpect(status().isUnauthorized());
	}

	@Test
	void login_Return200_Ok() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON).content("""
			                                                                                                      	{
			                                                                                                      		"username": "hunter",
			                                                                                                      		"password": "strongPass1"
			                                                                                                      	}
			                                                                                                      """))
		       .andExpect(status().isOk()).andExpect(jsonPath("$.accessToken", notNullValue()))
		       .andExpect(jsonPath("$.refreshToken", notNullValue()));
	}

	@Test
	void refreshToken_Return401_HeaderAuthorizationNotPresent() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/refresh-token").contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isUnauthorized());
	}

	@Test
	void refreshToken_Return401_HeaderAuthorizationEmpty() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/refresh-token").header("Authorization", "")
		                                      .contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isUnauthorized());
	}

	@Test
	void refreshToken_Return401_SignInvalid() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/refresh-token").contentType(MediaType.APPLICATION_JSON)
		                                      .header("Authorization",
		                                              "Bearer eyJhbGciOiJIUzM4NCJ9" +
		                                              ".eyJzdWIiOiJ5YWt1cF9qciIsImlhdCI6MTczODIzMDk2MiwiZXhwI" +
		                                              ".jsfasfuhi23haFDSFg23_12"))
		       .andExpect(status().isUnauthorized());
	}

	@Test
	void refreshToken_Return401_TokenExpired() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/refresh-token").contentType(MediaType.APPLICATION_JSON).header(
			       "Authorization", "Bearer eyJhbGciOiJIUzM4NCJ9" +
			                        ".eyJzdWIiOiJ5YWt1cF9qciIsImlhdCI6MTczODIzMDk2MiwiZXhwIjoxNzM4MjY2OTYyfQ" +
			                        ".bEpB6h2R0560lx-E9VPdeKYtfkoIUUebvOUWqr5W0irJDvlmLmka__dtWrQUPeKT"))
		       .andExpect(status().isUnauthorized());
	}

	@Test
	@Disabled
	void refreshToken_Return200_Ok() throws Exception {
		AuthenticationResponseDto responseDto = authService.authenticate(
			new LoginRequestDto("hunter", "strongPass1"));
		mockMvc.perform(MockMvcRequestBuilders.post("/refresh-token").contentType(MediaType.APPLICATION_JSON).header(
			       "Authorization", "Bearer " + responseDto.refreshToken())).andExpect(status().isOk())
		       .andExpect(jsonPath("$.accessToken", notNullValue()))
		       .andExpect(jsonPath("$.refreshToken", notNullValue()));
	}

	@Test
	void authenticationOauth_Return400_BodyEmpty() throws Exception {
		mockMvc.perform(
			       MockMvcRequestBuilders.post("/login/oauth2/code/google").contentType(MediaType.APPLICATION_JSON).content(
				       """
					       """))
		       .andExpect(status().isBadRequest());
	}
}
