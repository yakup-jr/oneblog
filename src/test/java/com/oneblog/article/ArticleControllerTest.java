package com.oneblog.article;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.oneblog.article.dto.ArticleCreateDto;
import com.oneblog.article.label.LabelName;
import com.oneblog.article.label.dto.LabelDto;
import com.oneblog.article.preview.dto.PreviewCreateDto;
import com.oneblog.helpers.IntegrationTest;
import com.oneblog.helpers.WithMockAdmin;
import com.oneblog.user.dto.UserDto;
import com.oneblog.user.role.RoleDto;
import com.oneblog.user.role.RoleName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@WithMockUser
public class ArticleControllerTest {

	@Autowired
	private MockMvc mockMvc;

	private static final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

	private ArticleCreateDto createValidArticleCreateDto() {
		return ArticleCreateDto.builder()
		                       .title("the best president")
		                       .body("more and more text...")
		                       .createTime(LocalDateTime.now())
		                       .preview(new PreviewCreateDto("Something interesting preview"))
		                       .labels(List.of(LabelDto.builder().labelId(1L).name(LabelName.Assembler).build(),
		                                       LabelDto.builder().labelId(2L).name(LabelName.C).build()))
		                       .user(UserDto.builder().userId(1L).name("James").nickname("hunter")
		                                    .email("hunter@mail.com")
		                                    .roles(List.of(RoleDto.builder().name(RoleName.ROLE_USER).build(),
		                                                   RoleDto.builder().name(RoleName.ROLE_ADMIN).build()))
		                                    .build())
		                       .build();
	}

	@Test
	@WithMockAdmin
	void createArticle_ReturnArticle() throws Exception {
		ArticleCreateDto articleCreateDto = createValidArticleCreateDto();

		mockMvc.perform(post("/api/v1/article/")
			                .contentType(MediaType.APPLICATION_JSON)
			                .content(objectMapper.writeValueAsString(articleCreateDto)))
		       .andExpect(status().isCreated())
		       .andExpect(jsonPath("$.articleId", notNullValue()))
		       .andExpect(jsonPath("$.preview.articlePreviewId", notNullValue()))
		       .andExpect(jsonPath("$.preview.body", notNullValue()))
		       .andExpect(jsonPath("$.labels", hasSize(2)))
		       .andExpect(jsonPath("$.labels[0].labelId", is(1)))
		       .andExpect(jsonPath("$._links.self.href", notNullValue()));
	}


	@Test
	@WithMockAdmin
	void createArticle_ThrowMethodArgumentNotValidException_TitleBlank() throws Exception {
		ArticleCreateDto articleCreateDto = createValidArticleCreateDto();
		articleCreateDto.setTitle("");

		mockMvc.perform(post("/api/v1/article/")
			                .contentType(MediaType.APPLICATION_JSON)
			                .content(objectMapper.writeValueAsString(articleCreateDto)))
		       .andExpect(status().isBadRequest())
		       .andExpect(jsonPath("$.message", containsString("title: length must be between 1 and 255")));
	}

	@Test
	@WithMockAdmin
	void createArticle_ThrowMethodArgumentNotValidException_PreviewBodyBlank() throws Exception {
		ArticleCreateDto articleCreateDto = createValidArticleCreateDto();
		articleCreateDto.getPreview().setBody("");

		mockMvc.perform(post("/api/v1/article/")
			                .contentType(MediaType.APPLICATION_JSON)
			                .content(objectMapper.writeValueAsString(articleCreateDto)))
		       .andExpect(status().isBadRequest())
		       .andExpect(jsonPath("$.message", containsString("preview.body: length must be between 10 and 1000")));
	}

	@Test
	@WithMockAdmin
	void createArticle_ThrowMethodArgumentNotValidException_LabelIdNull() throws Exception {
		ArticleCreateDto articleCreateDto = createValidArticleCreateDto();
		articleCreateDto.setLabels(null);

		mockMvc.perform(post("/api/v1/article/")
			                .contentType(MediaType.APPLICATION_JSON)
			                .content(objectMapper.writeValueAsString(articleCreateDto)))
		       .andExpect(status().isBadRequest())
		       .andExpect(
			       jsonPath("$.message", containsString("labels: must not be null")));
	}

	@Test
	@WithMockAdmin
	void createArticle_ThrowMethodArgumentNotValidException_LabelIdZero() throws Exception {
		ArticleCreateDto articleCreateDto = createValidArticleCreateDto();
		articleCreateDto.setLabels(List.of(LabelDto.builder().labelId(0L).build()));

		mockMvc.perform(post("/api/v1/article/")
			                .contentType(MediaType.APPLICATION_JSON)
			                .content(objectMapper.writeValueAsString(articleCreateDto)))
		       .andExpect(status().isBadRequest())
		       .andExpect(jsonPath("$.message", containsString("labels[0].labelId: must be greater than or equal to " +
		                                                       "1")));
	}

	@Test
	@WithMockAdmin
	void createArticle_ThrowMethodArgumentNotValidException_UserIdNull() throws Exception {
		ArticleCreateDto articleCreateDto = createValidArticleCreateDto();
		articleCreateDto.setUser(null);

		mockMvc.perform(post("/api/v1/article/")
			                .contentType(MediaType.APPLICATION_JSON)
			                .content(objectMapper.writeValueAsString(articleCreateDto)))
		       .andExpect(status().isBadRequest())
		       .andExpect(jsonPath("$.message", containsString("user: must not be null")));
	}

	@Test
	@WithMockAdmin
	void createArticle_ThrowMethodArgumentNotValidException_UserIdZero() throws Exception {
		ArticleCreateDto articleCreateDto = createValidArticleCreateDto();
		articleCreateDto.setUser(UserDto.builder().userId(0L).build());

		mockMvc.perform(post("/api/v1/article/")
			                .contentType(MediaType.APPLICATION_JSON)
			                .content(objectMapper.writeValueAsString(articleCreateDto)))
		       .andExpect(status().isBadRequest())
		       .andExpect(jsonPath("$.message", containsString("user.userId: must be greater than or equal to 1")));
	}

	@Test
	@WithMockAdmin
	void createArticle_ThrowApiRequestException_articlePreview() throws Exception {
		ArticleCreateDto articleCreateDto = createValidArticleCreateDto();
		articleCreateDto.setPreview(null);

		mockMvc.perform(post("/api/v1/article/")
			                .contentType(MediaType.APPLICATION_JSON)
			                .content(objectMapper.writeValueAsString(articleCreateDto)))
		       .andExpect(status().isBadRequest())
		       .andExpect(jsonPath("$.message", containsStringIgnoringCase("preview")));
	}

	@Test
	@WithMockAdmin
	void createArticle_ThrowApiRequestException_labels() throws Exception {
		ArticleCreateDto articleCreateDto = createValidArticleCreateDto();
		articleCreateDto.setLabels(null);

		mockMvc.perform(post("/api/v1/article/")
			                .contentType(MediaType.APPLICATION_JSON)
			                .content(objectMapper.writeValueAsString(articleCreateDto)))
		       .andExpect(status().isBadRequest())
		       .andExpect(jsonPath("$.message", containsStringIgnoringCase("label")));
	}

	@Test
	@WithMockAdmin
	void createArticle_ThrowApiRequestException_user() throws Exception {
		ArticleCreateDto articleCreateDto = createValidArticleCreateDto();
		articleCreateDto.setUser(null);

		mockMvc.perform(post("/api/v1/article/")
			                .contentType(MediaType.APPLICATION_JSON)
			                .content(objectMapper.writeValueAsString(articleCreateDto)))
		       .andExpect(status().isBadRequest())
		       .andExpect(jsonPath("$.message", containsStringIgnoringCase("user")));
	}

	@Test
	void createArticle_ReturnForbidden_NoPermission() throws Exception {
		ArticleCreateDto articleCreateDto = createValidArticleCreateDto();

		mockMvc.perform(post("/api/v1/article/")
			                .contentType(MediaType.APPLICATION_JSON)
			                .content(objectMapper.writeValueAsString(articleCreateDto)))
		       .andExpect(status().isForbidden());
	}

	@Test
	void findArticleByArticleId_ReturnArticle() throws Exception {
		mockMvc.perform(get("/api/v1/article/2").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
		       .andExpect(jsonPath("$.articleId", is(2))).andExpect(jsonPath("$.preview.articlePreviewId", is(2)))
		       .andExpect(jsonPath("$.preview.body", notNullValue())).andExpect(jsonPath("$.labels", hasSize(2)))
		       .andExpect(jsonPath("$.labels[0].labelId", is(2))).andExpect(jsonPath("$.user.userId", is(2)))
		       .andExpect(jsonPath("$._links.self.href", notNullValue()));
	}

	@Test
	void findArticleByArticleId_ThrowArticleNotFoundException() throws Exception {
		mockMvc.perform(get("/api/v1/article/999").contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isNotFound());
	}

	@Test
	void findAllArticles_ReturnArticles() throws Exception {
		mockMvc.perform(get("/api/v1/articles?page=0&size=3").contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isOk()).andExpect(jsonPath("$._embedded.articles", hasSize(3)))
		       .andExpect(jsonPath("$._embedded.articles[0].articleId", is(5)))
		       .andExpect(jsonPath("$._embedded.articles[0].preview", notNullValue()))
		       .andExpect(jsonPath("$._embedded.articles[0].user", notNullValue()));
	}

	@Test
	void findAllArticles_ReturnBadRequest_PageNotExists() throws Exception {
		mockMvc.perform(get("/api/v1/articles").contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isBadRequest());
	}

	@Test
	void findAllArticles_ReturnBadRequest_PageLessZero() throws Exception {
		mockMvc.perform(get("/api/v1/articles?page=-1").contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isBadRequest()).andExpect(jsonPath("$.message", containsStringIgnoringCase("page")));
	}

	@Test
	void findAllArticles_ReturnBadRequest_PageMoreMax() throws Exception {
		mockMvc.perform(get("/api/v1/articles?page=999&size=3").contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isBadRequest()).andExpect(jsonPath("$.message", containsStringIgnoringCase("page")));
	}

	@Test
	void findArticlesByUserId_ReturnArticles() throws Exception {
		mockMvc.perform(get("/api/v1/article/user/1").contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isOk()).andExpect(jsonPath("$._embedded.articles[0].articleId", notNullValue()))
		       .andExpect(jsonPath("$._embedded.articles", hasSize(1)))
		       .andExpect(jsonPath("$._embedded.articles[0].preview.articlePreviewId", notNullValue()))
		       .andExpect(jsonPath("$._embedded.articles[0]._links.self.href", notNullValue()));
	}

	@Test
	void findArticlesByUserId_ThrowArticleNotFoundException() throws Exception {
		mockMvc.perform(get("/api/v1/article/user/999").contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isNotFound());
	}

	@Test
	@WithMockAdmin
	void deleteArticleByArticleId_ReturnNoContent() throws Exception {
		mockMvc.perform(delete("/api/v1/article/2").contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isNoContent());
	}

	@Test
	@WithMockAdmin
	void deleteArticleByArticleId_ThrowArticleNotFoundException() throws Exception {
		mockMvc.perform(delete("/api/v1/article/999").contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isNotFound());
	}

	@Test
	void deleteArticleByArticleId_ReturnForbidden_NoPermission() throws Exception {
		mockMvc.perform(delete("/api/v1/article/2").contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isForbidden());
	}

}
