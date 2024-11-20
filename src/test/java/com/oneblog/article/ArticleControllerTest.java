package com.oneblog.article;


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
@Sql(scripts = "/static/testdb/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@ExtendWith(DatabaseCleanerExtension.class)
public class ArticleControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void createArticle_ReturnArticle() throws Exception {
		mockMvc.perform(post("/api/v1/article/").contentType(MediaType.APPLICATION_JSON).content("""
			                                                                                         		{
			                                                                                         			"title": "the best president",
			                                                                                         			"body": "more and more text...",
			                                                                                         			"articlePreview": {
			                                                                                         				"body": "Something interesting preview"
			                                                                                         			},
			                                                                                         			"labels": [
			                                                                                         				{
			                                                                                         					"labelId": 1
			                                                                                         				},
			                                                                                         				{
			                                                                                         					"labelId": 2
			                                                                                         				}
			                                                                                         			],
			                                                                                         			"user": {
			                                                                                         				"userId": 1
			                                                                                         			}
			                                                                                         		}
			                                                                                         """))
		       .andExpect(status().isCreated()).andExpect(jsonPath("$.articleId", notNullValue()))
		       .andExpect(jsonPath("$.articlePreview.articlePreviewId", notNullValue()))
		       .andExpect(jsonPath("$.articlePreview.body", notNullValue())).andExpect(jsonPath("$.labels", hasSize(2)))
		       .andExpect(jsonPath("$.labels[0].labelId", is(1)))
		       .andExpect(jsonPath("$._links.self.href", notNullValue()));
	}

	@Test
	void createArticle_ThrowApiRequestException_articlePreview() throws Exception {
		mockMvc.perform(post("/api/v1/article/").contentType(MediaType.APPLICATION_JSON).content("""
			                                                                                          {
			                                                                                          			"title": "the best president",
			                                                                                          			"body": "more and more text...",
			                                                                                          			"labels": [
			                                                                                          				{
			                                                                                          					"labelId": 1
			                                                                                          				},
			                                                                                          				{
			                                                                                          					"labelId": 2
			                                                                                          				}
			                                                                                          			],
			                                                                                          			"user": {
			                                                                                          				"userId": 1
			                                                                                          			}
			                                                                                            	 }
			                                                                                         """))
		       .andExpect(status().isBadRequest())
		       .andExpect(jsonPath("$.message", containsStringIgnoringCase("preview")));

	}

	@Test
	void createArticle_ThrowApiRequestException_labels() throws Exception {
		mockMvc.perform(post("/api/v1/article/").contentType(MediaType.APPLICATION_JSON).content("""
			                                                                                         	{
			                                                                                         			"title": "the best president",
			                                                                                         			"body": "more and more text...",
			                                                                                         			"articlePreview": {
			                                                                                         				"body": "Something interesting preview"
			                                                                                         			},
			                                                                                         			"user": {
			                                                                                         				"userId": 1
			                                                                                         			}
			                                                                                         		}
			                                                                                         """))
		       .andExpect(status().isBadRequest())
		       .andExpect(jsonPath("$.message", containsStringIgnoringCase("label")));
	}

	@Test
	void createArticle_ThrowApiRequestException_user() throws Exception {
		mockMvc.perform(post("/api/v1/article/").contentType(MediaType.APPLICATION_JSON).content("""
			                                                                                         	{
			                                                                                         		"title": "the best president",
			                                                                                         		"body": "more and more text...",
			                                                                                         		"articlePreview": {
			                                                                                         			"body": "Something interesting preview"
			                                                                                         		},
			                                                                                         		"labels": [
			                                                                                         			{
			                                                                                         				"labelId": "1"
			                                                                                         			}
			                                                                                         		],
			                                                                                         		"user": {
			                                                                                         			"userId": null
			                                                                                         		}
			                                                                                         	}
			                                                                                         """))
		       .andExpect(status().isBadRequest()).andExpect(jsonPath("$.message", containsStringIgnoringCase("user")));
	}

	@Test
	void findArticleByArticleId_ReturnArticle() throws Exception {
		mockMvc.perform(get("/api/v1/article/2").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
		       .andExpect(jsonPath("$.articleId", is(2)))
		       .andExpect(jsonPath("$.articlePreview.articlePreviewId", is(2)))
		       .andExpect(jsonPath("$.articlePreview.body", notNullValue())).andExpect(jsonPath("$.labels", hasSize(2)))
		       .andExpect(jsonPath("$.labels[0].labelId", is(2))).andExpect(jsonPath("$.user.userId", is(2)))
		       .andExpect(jsonPath("$._links.self.href", notNullValue()));
	}

	@Test
	void findArticleByArticleId_ThrowArticleNotFoundException() throws Exception {
		mockMvc.perform(get("/api/v1/article/999").contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isNotFound());
	}

	@Test
	void findArticlesByUserId_ReturnArticles() throws Exception {
		mockMvc.perform(get("/api/v1/article/user/1").contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isOk()).andExpect(jsonPath("$._embedded.articles[0].articleId", notNullValue()))
		       .andExpect(jsonPath("$._embedded.articles", hasSize(1)))
		       .andExpect(jsonPath("$._embedded.articles[0].articlePreview.articlePreviewId", notNullValue()))
		       .andExpect(jsonPath("$._embedded.articles[0]._links.self.href", notNullValue()));
	}

	@Test
	void findArticlesByUserId_ThrowArticleNotFoundException() throws Exception {
		mockMvc.perform(get("/api/v1/article/user/999").contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isNotFound());
	}

	@Test
	void deleteArticleByArticleId_ReturnNoContent() throws Exception {
		mockMvc.perform(delete("/api/v1/article/2").contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isNoContent());
	}

	@Test
	void deleteArticleByArticleId_ThrowArticleNotFoundException() throws Exception {
		mockMvc.perform(delete("/api/v1/article/999").contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isNotFound());
	}

}
