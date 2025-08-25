package net.oneblog.article.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.oneblog.api.interfaces.LabelName;
import net.oneblog.article.models.ArticleCreateModel;
import net.oneblog.article.models.LabelModel;
import net.oneblog.article.models.PreviewCreateModel;
import net.oneblog.sharedconfig.test.IntegrationTest;
import net.oneblog.validationapi.models.ValidatedUserModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
public class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    private ArticleCreateModel createValidArticleCreateDto() {
        return ArticleCreateModel.builder()
            .title("the best president")
            .body("more and more text...")
            .preview(new PreviewCreateModel("Something interesting preview"))
            .labels(List.of(
                LabelModel.builder().labelId(1L).name(LabelName.Assembler).build(),
                LabelModel.builder().labelId(2L).name(LabelName.C).build()))
            .user(ValidatedUserModel.builder().userId(2L).name("Emily").nickname("shadow")
                .email("shadow@mail.com").build())
            .build();
    }

    @Test
    void createArticle_ReturnArticle() throws Exception {
        ArticleCreateModel articleCreateModel = createValidArticleCreateDto();

        mockMvc.perform(post("/api/v1/article")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(articleCreateModel)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.articleId", notNullValue()))
            .andExpect(jsonPath("$.preview.body", notNullValue()))
            .andExpect(jsonPath("$.labels", hasSize(2)))
            .andExpect(jsonPath("$._links.self.href", notNullValue()));
    }

    @Test
    void createArticle_ThrowMethodArgumentNotValidException_TitleBlank() throws Exception {
        ArticleCreateModel articleCreateModel = createValidArticleCreateDto();
        articleCreateModel.setTitle("");

        mockMvc.perform(post("/api/v1/article")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(articleCreateModel)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", containsString("title")));
    }

    @Test
    void createArticle_ThrowMethodArgumentNotValidException_PreviewBodyBlank() throws Exception {
        ArticleCreateModel articleCreateModel = createValidArticleCreateDto();
        articleCreateModel.getPreview().setBody("");

        mockMvc.perform(post("/api/v1/article")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(articleCreateModel)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", containsString("body")));
    }

    @Test
    void createArticle_ThrowMethodArgumentNotValidException_LabelIdNull() throws Exception {
        ArticleCreateModel articleCreateModel = createValidArticleCreateDto();
        articleCreateModel.setLabels(null);

        mockMvc.perform(post("/api/v1/article")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(articleCreateModel)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", containsString("labels")));
    }

    @Test
    void createArticle_ThrowMethodArgumentNotValidException_UserIdNull() throws Exception {
        ArticleCreateModel articleCreateModel = createValidArticleCreateDto();
        articleCreateModel.setUser(null);

        mockMvc.perform(post("/api/v1/article")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(articleCreateModel)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", containsString("user")));
    }

    @Test
    void findArticleByArticleId_ReturnArticle() throws Exception {
        mockMvc.perform(get("/api/v1/article/2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.articleId", is(2)))
            .andExpect(jsonPath("$.preview.body", notNullValue()))
            .andExpect(jsonPath("$.labels", hasSize(greaterThan(0))))
            .andExpect(jsonPath("$._links.self.href", notNullValue()));
    }

    @Test
    void findArticleByArticleId_ThrowArticleNotFoundException() throws Exception {
        mockMvc.perform(get("/api/v1/article/999"))
            .andExpect(status().isNotFound());
    }

    @Test
    void findAllArticles_ReturnArticles() throws Exception {
        mockMvc.perform(get("/api/v1/articles?page=0&size=3"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$._embedded.articles", hasSize(greaterThan(0))))
            .andExpect(jsonPath("$._embedded.articles[0].articleId", notNullValue()));
    }

    @Test
    void deleteArticle_ReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/article/1"))
            .andExpect(status().isNoContent());
    }

    @Test
    void deleteArticle_ThrowArticleNotFoundException() throws Exception {
        mockMvc.perform(delete("/api/v1/article/999"))
            .andExpect(status().isNotFound());
    }
}
