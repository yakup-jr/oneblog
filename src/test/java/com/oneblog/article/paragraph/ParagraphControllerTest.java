package com.oneblog.article.paragraph;

import com.oneblog.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {TestConfig.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(scripts = "/static/testdb/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
public class ParagraphControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void createParagraph_ReturnParagraph() throws Exception {
		mockMvc.perform(post("/api/v1/paragraph").contentType(MediaType.APPLICATION_JSON).content("""
		                                                                                          	{
		                                                                                          		"text": "Lorem ipsum 100",
		                                                                                          		"article": {
		                                                                                          			"articleId": 1
		                                                                                          		}
		                                                                                          	}
		                                                                                          """))
		       .andExpect(status().isCreated()).andExpect(jsonPath("$.paragraphId", notNullValue()))
		       .andExpect(jsonPath("$.text", containsString("Lorem ipsum 100")))
		       .andExpect(jsonPath("$._links.self.href", containsString("/api/v1/paragraph/")));
	}

	@Test
	public void createParagraph_ThrowParagraphAttachedConflictException_MoreThanOneAttached() throws Exception {
		mockMvc.perform(post("/api/v1/paragraph").contentType(MediaType.APPLICATION_JSON).content("""
		                                                                                          	{
		                                                                                          		"text": "Lorem ipsum 100",
		                                                                                          		"article": {
		                                                                                          			"articleId": 1
		                                                                                          		},
		                                                                                          		"articlePreview": {
		                                                                                          			"articlePreviewId": 1
		                                                                                          		}
		                                                                                          	}
		                                                                                          """))
		       .andExpect(status().isConflict()).andExpect(jsonPath("$.message", containsString("only to one entity")));
	}

	@Test
	public void createParagraph_ThrowParagraphAttachedConflictException_NothingAttached() throws Exception {
		mockMvc.perform(post("/api/v1/paragraph").contentType(MediaType.APPLICATION_JSON).content("""
		                                                                                          	{
		                                                                                          		"text": "Lorem ipsum 100"
		                                                                                          	}
		                                                                                          """))
		       .andExpect(jsonPath("$.message", containsString("at least at one entity")));
	}

	@Test
	public void findParagraphById_ReturnParagraph() throws Exception {
		mockMvc.perform(get("/api/v1/paragraph/1").contentType(MediaType.APPLICATION_JSON))
		       .andExpect(jsonPath("$.paragraphId", notNullValue()))
		       .andExpect(jsonPath("$._links.self.href", containsString("/api/v1/paragraph")));
	}

	@Test
	public void findParagraphById_ThrowParagraphNotFoundException() throws Exception {
		mockMvc.perform(get("/api/v1/paragraph/999").contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isNotFound());
	}

	@Test
	public void findParagraphByArticleId_ReturnParagraphs() throws Exception {
		mockMvc.perform(get("/api/v1/paragraph/article/1").contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isOk()).andExpect(jsonPath("$._embedded.paragraphs", notNullValue()))
		       .andExpect(jsonPath("$._embedded.paragraphs", notNullValue()))
		       .andExpect(jsonPath("$._embedded.paragraphs[0]._links.self", notNullValue()))
		       .andExpect(jsonPath("$._links.article.href", notNullValue()))
		       .andExpect(jsonPath("$._links.self.href", notNullValue()));
	}

	@Test
	public void findParagraphByArticleId_ThrowParagraphsNotFoundException() throws Exception {
		mockMvc.perform(get("/api/v1/paragraph/article/999").contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isNotFound());
	}

	@Test
	public void findParagraphByArticlePreviewId_ReturnParagraphs() throws Exception {
		mockMvc.perform(get("/api/v1/paragraph/article/preview/1").contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isOk()).andExpect(jsonPath("$._embedded.paragraphs", notNullValue()))
		       .andExpect(jsonPath("$._embedded.paragraphs[0]", notNullValue()))
		       .andExpect(jsonPath("$._embedded.paragraphs[0]._links.self", notNullValue()))
		       .andExpect(jsonPath("$._links.articlePreview.href", notNullValue()))
		       .andExpect(jsonPath("$._links.self.href", notNullValue()));
	}

	@Test
	public void findParagraphByArticlePreviewId_ThrowParagraphsNotFoundException() throws Exception {
		mockMvc.perform(get("/api/v1/paragraph/article/999").contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isNotFound());
	}

	@Test
	public void deleteParagraphById_ReturnParagraph_ArticleLink() throws Exception {
		mockMvc.perform(delete("/api/v1/paragraph/3").contentType(MediaType.APPLICATION_JSON))
		       .andExpect(jsonPath("$.text", notNullValue()))
		       .andExpect(jsonPath("$._links.article.href", containsString("/api/v1/article")));
	}

	@Test
	public void deleteParagraphById_ReturnParagraph_ArticlePreviewLink() throws Exception {
		mockMvc.perform(delete("/api/v1/paragraph/5").contentType(MediaType.APPLICATION_JSON))
		       .andExpect(jsonPath("$.text", notNullValue()))
		       .andExpect(jsonPath("$._links.articlePreview.href", containsString("/api/v1/article/preview")));
	}

	@Test
	public void deleteParagraphById_ThrowParagraphNotFoundException() throws Exception {
		mockMvc.perform(delete("/api/v1/paragraph/999").contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isNotFound());
	}
}
