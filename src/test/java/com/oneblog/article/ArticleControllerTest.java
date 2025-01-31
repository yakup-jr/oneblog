package com.oneblog.article;


import com.oneblog.helpers.IntegrationTest;
import com.oneblog.helpers.WithMockAdmin;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
public class ArticleControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	@WithMockAdmin
	void createArticle_ReturnArticle() throws Exception {
		mockMvc.perform(post("/api/v1/article/").contentType(MediaType.APPLICATION_JSON).content(
			       "		{\n" +
			       "			\"title\": \"the best president\",\n" +
			       "			\"body\": \"more and more text...\",\n" +
			       "			\"preview\": {\n" +
			       "				\"body\": \"Something interesting preview\"\n" +
			       "			},\n" +
			       "			\"labels\": [\n" +
			       "				{\n" +
			       "					\"labelId\": 1\n" +
			       "				},\n" +
			       "				{\n" +
			       "					\"labelId\": 2\n" +
			       "				}\n" +
			       "			],\n" +
			       "			\"user\": {\n" +
			       "				\"userId\": 1\n" +
			       "			}\n" +
			       "		}\n"))
		       .andExpect(status().isCreated()).andExpect(jsonPath("$.articleId", notNullValue()))
		       .andExpect(jsonPath("$.preview.articlePreviewId", notNullValue()))
		       .andExpect(jsonPath("$.preview.body", notNullValue())).andExpect(jsonPath("$.labels", hasSize(2)))
		       .andExpect(jsonPath("$.labels[0].labelId", is(1)))
		       .andExpect(jsonPath("$._links.self.href", notNullValue()));
	}

	@Test
	@WithMockAdmin
	void createArticle_ThrowMethodArgumentNotValidException_TitleBlank() throws Exception {
		mockMvc.perform(post("/api/v1/article/").contentType(MediaType.APPLICATION_JSON).content(
			       "	{\n" +
			       "		\"title\": \"\",\n" +
			       "		\"body\": \"\",\n" +
			       "		\"preview\": {\n" +
			       "		\"body\": \"Something interesting preview\"\n" +
			       "		},\n" +
			       "		\"labels\": [\n" +
			       "		{\n" +
			       "			\"labelId\": 1\n" +
			       "		},\n" +
			       "		{\n" +
			       "			\"labelId\": 2\n" +
			       "		}\n" +
			       "		],\n" +
			       "		\"user\": {\n" +
			       "			\"userId\": 1\n" +
			       "		}\n" +
			       "	}\n"))
		       .andExpect(status().isBadRequest())
		       .andExpect(jsonPath("$.message", containsString("title: length must be between 1 and 255")));
	}

	@Test
	@WithMockAdmin
	void createArticle_ThrowMethodArgumentNotValidException_PreviewBodyBlank() throws Exception {
		mockMvc.perform(post("/api/v1/article/").contentType(MediaType.APPLICATION_JSON).content(
			       "	{\n" +
			       "		\"title\": \"the best president\",\n" +
			       "		\"body\": \"more and more text...\",\n" +
			       "		\"preview\": {\n" +
			       "			\"body\": \"\"\n" +
			       "       },\n" +
			       "       \"labels\": [\n" +
			       "       	{\n" +
			       "       		\"labelId\": 1\n" +
			       "       	},\n" +
			       "       	{\n" +
			       "       		\"labelId\": 2\n" +
			       "       	}\n" +
			       "       ],\n" +
			       "       \"user\": {\n" +
			       "       	\"userId\": 1\n" +
			       "       }\n" +
			       "}\n"))
		       .andExpect(status().isBadRequest())
		       .andExpect(jsonPath("$.message", containsString("preview.body: length must be between 10 and 1000")));
	}

	@Test
	@WithMockAdmin
	void createArticle_ThrowMethodArgumentNotValidException_LabelId() throws Exception {
		mockMvc.perform(post("/api/v1/article/").contentType(MediaType.APPLICATION_JSON).content("""
			                                                                                         	{
			                                                                                          	"title": "the best president",
			                                                                                          	"body": "more and more text...",
			                                                                                          	"preview": {
			                                                                                          		"body": "Something interesting preview"
			                                                                                          	},
			                                                                                          	"labels": [
			                                                                                          		{
			                                                                                          			"labelId": 0
			                                                                                          		}
			                                                                                          	],
			                                                                                          	"user": {
			                                                                                          		"userId": 1
			                                                                                          	}
			                                                                                         	}
			                                                                                         """))
		       .andExpect(status().isBadRequest()).andExpect(
			       jsonPath("$.message", containsString("labels[0].labelId: must be greater than or equal to 1")));
	}

	@Test
	@WithMockAdmin
	void createArticle_ThrowMethodArgumentNotValidException_UserId() throws Exception {
		mockMvc.perform(post("/api/v1/article/").contentType(MediaType.APPLICATION_JSON).content("""
			                                                                                         	{
			                                                                                         	"body": "more and more text...",
			                                                                                         	"labels": [
			                                                                                         		{
			                                                                                         			"labelId": 1
			                                                                                         		},
			                                                                                         		{
			                                                                                         			"labelId": 2
			                                                                                         		}
			                                                                                         	],
			                                                                                         	"preview": {
			                                                                                         		"body": "Something interesting preview"
			                                                                                         	},
			                                                                                         	"title": "the best president",
			                                                                                         	"user": {
			                                                                                         		"userId": 0
			                                                                                         	}
			                                                                                         	}
			                                                                                         """))
		       .andExpect(status().isBadRequest())
		       .andExpect(jsonPath("$.message", containsString("user.userId: must be greater than or equal to 1")));
	}

	@Test
	@WithMockAdmin
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
	@WithMockAdmin
	void createArticle_ThrowApiRequestException_labels() throws Exception {
		mockMvc.perform(post("/api/v1/article/").contentType(MediaType.APPLICATION_JSON).content("""
			                                                                                         	{
			                                                                                         			"title": "the best president",
			                                                                                         			"body": "more and more text...",
			                                                                                         			"preview": {
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
	@WithMockAdmin
	void createArticle_ThrowApiRequestException_user() throws Exception {
		mockMvc.perform(post("/api/v1/article/").contentType(MediaType.APPLICATION_JSON).content("""
			                                                                                         	{
			                                                                                         		"title": "the best president",
			                                                                                         		"body": "more and more text...",
			                                                                                         		"preview": {
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
	void createArticle_ReturnForbidden_NoPermission() throws Exception {
		mockMvc.perform(post("/api/v1/article/").contentType(MediaType.APPLICATION_JSON).content(
			       "		{\n" +
			       "			\"title\": \"the best president\",\n" +
			       "			\"body\": \"more and more text...\",\n" +
			       "			\"preview\": {\n" +
			       "				\"body\": \"Something interesting preview\"\n" +
			       "			},\n" +
			       "			\"labels\": [\n" +
			       "				{\n" +
			       "					\"labelId\": 1\n" +
			       "				},\n" +
			       "				{\n" +
			       "					\"labelId\": 2\n" +
			       "				}\n" +
			       "			],\n" +
			       "			\"user\": {\n" +
			       "				\"userId\": 1\n" +
			       "			}\n" +
			       "		}\n"))
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
