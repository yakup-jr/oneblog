package com.oneblog.user;

import com.oneblog.DatabaseCleanerExtension;
import com.oneblog.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = {TestConfig.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(value = "/static/testdb/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@ExtendWith(DatabaseCleanerExtension.class)
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void saveUser_ReturnUser() throws Exception {
		mockMvc.perform(post("/api/v1/user/").contentType(MediaType.APPLICATION_JSON).content("""
			                                                                                      {
			                                                                                      	"name": "Alex",
			                                                                                      	"nickname": "simple",
			                                                                                      	"email": "simple@mail.com",
			                                                                                      	"password": "strongPass"
			                                                                                      }
			                                                                                      """))
		       .andExpect(status().isCreated()).andExpect(jsonPath("$.userId", notNullValue()))
		       .andExpect(jsonPath("$.roles", hasSize(1))).andExpect(jsonPath("$.roles[0].name", is("ROLE_USER")))
		       .andExpect(jsonPath("$._links.user.href", endsWith("user/6")));
	}

	@Test
	void saveUser_ThrowApiRequestException_NicknameIsNotUnique() throws Exception {
		mockMvc.perform(post("/api/v1/user/").contentType(MediaType.APPLICATION_JSON).content("""
			                                                                                      {
			                                                                                      	"name": "Alex",
			                                                                                      	"nickname": "hunter",
			                                                                                      	"email": "alex@mail.com",
			                                                                                      	"password": "strongPass"
			                                                                                      }
			                                                                                      """))
		       .andExpect(status().isBadRequest())
		       .andExpect(jsonPath("$.message", is("User nickname hunter already exists")));
	}

	@Test
	void saveUser_ThrowApiRequestException_EmailIsNotUnique() throws Exception {
		mockMvc.perform(post("/api/v1/user/").contentType(MediaType.APPLICATION_JSON).content("""
			                                                                                      	{
			                                                                                      		"name": "Alex",
			                                                                                      		"nickname": "simple",
			                                                                                      		"email": "hunter@mail.com",
			                                                                                      		"password": "strongPass"
			                                                                                      	}
			                                                                                      """))
		       .andExpect(status().isBadRequest())
		       .andExpect(jsonPath("$.message", is("User email hunter@mail.com already exists")));
	}

	@Test
	void findUserByUserId_ReturnUser() throws Exception {
		mockMvc.perform(get("/api/v1/user/1")).andExpect(status().isOk()).andExpect(jsonPath("$.userId", is(1)))
		       .andExpect(jsonPath("$._links.self.href", endsWith("user/1")))
		       .andExpect(jsonPath("$._links.user.href", endsWith("nickname/hunter")))
		       .andExpect(jsonPath("$._links.articles.href", endsWith("article/user/1")));
	}

	@Test
	void findUserByUserId_ThrowUserNotFoundException() throws Exception {
		mockMvc.perform(get("/api/v1/user/999")).andExpect(status().isNotFound());
	}

	@Test
	void findUserByNickname_ReturnUser() throws Exception {
		mockMvc.perform(get("/api/v1/user/nickname/hunter")).andExpect(status().isOk())
		       .andExpect(jsonPath("$.nickname", is("hunter")))
		       .andExpect(jsonPath("$._links.self.href", endsWith("nickname/hunter")))
		       .andExpect(jsonPath("$._links.user.href", endsWith("user/1")))
		       .andExpect(jsonPath("$._links.articles.href", endsWith("article/user/1")));
	}

	@Test
	void findUserByNickname_ThrowUserNotFoundException() throws Exception {
		mockMvc.perform(get("/api/v1/user/nickname/unknown")).andExpect(status().isNotFound());
	}

	@Test
	void findUserByArticleId_ReturnUser() throws Exception {
		mockMvc.perform(get("/api/v1/user/article/1")).andExpect(status().isOk()).andExpect(jsonPath("$.userId", is(1)))
		       .andExpect(jsonPath("$._links.self.href", endsWith("/user/article/1")));
	}

	@Test
	void findUserByArticleId_ThrowArticleNotFoundException() throws Exception {
		mockMvc.perform(get("/api/v1/user/article/999")).andExpect(status().isNotFound());
	}

	@Test
	void deleteUser_ReturnNoContent() throws Exception {
		mockMvc.perform(delete("/api/v1/user/1")).andExpect(status().isNoContent());
	}

	@Test
	void deleteUser_ThrowUserNotFoundException() throws Exception {
		mockMvc.perform(delete("/api/v1/user/999")).andExpect(status().isNotFound());
	}
}
