package com.oneblog.user;

import com.oneblog.helpers.IntegrationTest;
import com.oneblog.helpers.WithMockAdmin;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@IntegrationTest
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	@WithMockAdmin
	void saveUser_ReturnUser() throws Exception {
		mockMvc.perform(post("/api/v1/user").with(csrf()).contentType(MediaType.APPLICATION_JSON).content("{\n" +
		                                                                                                  "	\"name\": \"Alex\",\n" +
		                                                                                                  "	\"nickname\": \"simple\",\n" +
		                                                                                                  "	\"email\": \"simple@mail.com\",\n" +
		                                                                                                  "	\"password\": \"strongPass\"\n" +
		                                                                                                  "}\n"))
		       .andExpect(status().isCreated()).andExpect(jsonPath("$.userId", notNullValue()))
		       .andExpect(jsonPath("$.roles", hasSize(1))).andExpect(jsonPath("$.roles[0].name", is("ROLE_USER")))
		       .andExpect(jsonPath("$._links.user.href", endsWith("user/6")));
	}

	@Test
	@WithMockAdmin
	void saveUser_CsrfEmpty_Return403() throws Exception {
		mockMvc.perform(post("/api/v1/user").contentType(MediaType.APPLICATION_JSON).content("{\n" +
		                                                                                     "	\"name\": \"Alex\",\n" +
		                                                                                     "	\"nickname\": \"simple\",\n" +
		                                                                                     "	\"email\": \"simple@mail.com\",\n" +
		                                                                                     "	\"password\": \"strongPass\"\n" +
		                                                                                     "}\n"))
		       .andExpect(status().isForbidden());
	}

	@Test
	void saveUser_HaveNoPermission_Return403() throws Exception {
		mockMvc.perform(post("/api/v1/user").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(
			"{\"name\": \"Alex\",\n" +
			"\"nickname\": \"simple\",\n" +
			"\"email\": \"simple@mail.com\",\n" +
			"\"password\": \"strongPass\"}\n")).andExpect(status().isForbidden());
	}

	@Test
	@WithMockAdmin
	void saveUser_ThrowMethodArgumentNotValidException() throws Exception {
		mockMvc.perform(post("/api/v1/user").with(csrf()).contentType(MediaType.APPLICATION_JSON).content("	{\n" +
		                                                                                                  "		\"name\": \"\",\n" +
		                                                                                                  "		\"nickname\": \"\",\n" +
		                                                                                                  "		\"email\": \"somemail\",\n" +
		                                                                                                  "		\"password\": \"pass\"\n" +
		                                                                                                  "	}\n"))
		       .andExpect(status().isBadRequest())
		       .andExpect(jsonPath("$.message", containsString("name: length must be between 2 and 60")))
		       .andExpect(jsonPath("$.message", containsString("nickname: length must be between 2 and 60")))
		       .andExpect(jsonPath("$.message", containsString("email: must be a well-formed email address")))
		       .andExpect(jsonPath("$.message", containsString("password: length must be between 8 and 60")));
	}

	@Test
	@WithMockAdmin
	void saveUser_ThrowApiRequestException_NicknameIsNotUnique() throws Exception {
		mockMvc.perform(post("/api/v1/user").with(csrf()).contentType(MediaType.APPLICATION_JSON).content("{\n" +
		                                                                                                  "	\"name\": \"Alex\",\n" +
		                                                                                                  "	\"nickname\": \"hunter\",\n" +
		                                                                                                  "	\"email\": \"alex@mail.com\",\n" +
		                                                                                                  "	\"password\": \"strongPass\"\n" +
		                                                                                                  "}\n"))
		       .andExpect(status().isBadRequest())
		       .andExpect(jsonPath("$.message", is("User nickname hunter already exists")));
	}

	@Test
	@WithMockAdmin
	void saveUser_ThrowApiRequestException_EmailIsNotUnique() throws Exception {
		mockMvc.perform(post("/api/v1/user").with(csrf()).contentType(MediaType.APPLICATION_JSON).content("	{\n" +
		                                                                                                  "		\"name\": \"Alex\",\n" +
		                                                                                                  "		\"nickname\": \"simple\",\n" +
		                                                                                                  "		\"email\": \"hunter@mail.com\",\n" +
		                                                                                                  "		\"password\": \"strongPass\"\n" +
		                                                                                                  "	}\n"))
		       .andExpect(status().isBadRequest())
		       .andExpect(jsonPath("$.message", is("User email hunter@mail.com already exists")));
	}

	@Test
	@WithMockAdmin
	void findAllUsers_ReturnUsers() throws Exception {
		mockMvc.perform(get("/api/v1/users?page=0&size=3").contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isOk()).andExpect(jsonPath("$._embedded.users", hasSize(3)))
		       .andExpectAll(jsonPath("$._embedded.users[0].userId", is(1)),
		                     jsonPath("$._embedded.users[0].name", is("James")),
		                     jsonPath("$._embedded.users[0].nickname", is("hunter")),
		                     jsonPath("$._embedded.users[0].email", is("hunter@mail.com")),
		                     jsonPath("$._embedded.users[0].roles", hasSize(2)),
		                     jsonPath("$._embedded.users[0].roles[0].name", is("ROLE_ADMIN")));
	}

	@Test
	@WithMockAdmin
	void findAllUsers_ReturnBadRequest_PageLessZero() throws Exception {
		mockMvc.perform(get("/api/v1/users?page=-1&size=3").contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isBadRequest());
	}

	@Test
	@WithMockAdmin
	void findAllUsers_ReturnNotFound_PageMoreMax() throws Exception {
		mockMvc.perform(get("/api/v1/users?page=999").contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isNotFound());
	}

	@Test
	@WithMockAdmin
	void findAllUsers_ReturnNotFound_SizeLessOne() throws Exception {
		mockMvc.perform(get("/api/v1/users?page=0&size=-1").contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isBadRequest());
	}

	@Test
	@WithMockAdmin
	void findAllUsers_ReturnNotFound_SizeMoreMax() throws Exception {
		mockMvc.perform(get("/api/v1/users?page=0&size=999").contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isBadRequest());
	}

	@Test
	void findAllUsers_ReturnForbidden_NotPermission() throws Exception {
		mockMvc.perform(get("/api/v1/users?page=0&size=3").contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isForbidden());
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
	@WithMockAdmin
	void deleteUser_ReturnNoContent() throws Exception {
		mockMvc.perform(delete("/api/v1/user/1").with(csrf())).andExpect(status().isNoContent());
	}

	@Test
	@WithMockAdmin
	void deleteUser_ThrowUserNotFoundException() throws Exception {
		mockMvc.perform(delete("/api/v1/user/999").with(csrf())).andExpect(status().isNotFound());
	}

	@Test
	@WithMockAdmin
	void deleteUser_ReturnForbidden_CsrfEmpty() throws Exception {
		mockMvc.perform(delete("/api/v1/user/1")).andExpect(status().isForbidden());
	}

	@Test
	void deleteUser_ReturnForbidden_NotPermission() throws Exception {
		mockMvc.perform(delete("/api/v1/user/1").with(csrf())).andExpect(status().isForbidden());
	}
}
