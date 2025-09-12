package net.oneblog.article.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.oneblog.api.interfaces.VoteType;
import net.oneblog.article.models.VoteCreateModel;
import net.oneblog.sharedconfig.test.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
class VoteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createVote_ShouldReturn201() throws Exception {
        VoteCreateModel voteCreateModel = VoteCreateModel.builder()
            .userId(5L)
            .articleId(4L)
            .voteType(VoteType.LIKE)
            .build();

        mockMvc.perform(post("/api/v1/articles/vote")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(voteCreateModel)))
            .andExpect(status().isCreated());
    }

    @Test
    void createVote_ShouldReturn409_WhenVoteAlreadyExists() throws Exception {
        VoteCreateModel voteCreateModel = VoteCreateModel.builder()
            .userId(1L)
            .articleId(1L)
            .voteType(VoteType.LIKE)
            .build();

        mockMvc.perform(post("/api/v1/articles/vote")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(voteCreateModel)))
            .andExpect(status().isConflict());
    }

    @Test
    void findVoteByVoteId_ShouldReturnVote() throws Exception {
        mockMvc.perform(get("/api/v1/articles/vote/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.voteId").value(1))
            .andExpect(jsonPath("$._links").exists());
    }

    @Test
    void findVotesByArticleId_ShouldReturnVotes() throws Exception {
        mockMvc.perform(get("/api/v1/articles/vote/article/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void findVotesByUserId_ShouldReturnVotes() throws Exception {
        mockMvc.perform(get("/api/v1/articles/vote/user/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void deleteVoteByVoteId_ShouldReturn204() throws Exception {
        mockMvc.perform(delete("/api/v1/articles/vote/1"))
            .andExpect(status().isNoContent());
    }

    @Test
    void deleteVotesByUserId_ShouldReturn204() throws Exception {
        mockMvc.perform(delete("/api/v1/articles/vote/user/1"))
            .andExpect(status().isNoContent());
    }

    @Test
    void deleteVotesByArticleId_ShouldReturn204() throws Exception {
        mockMvc.perform(delete("/api/v1/articles/vote/article/1"))
            .andExpect(status().isNoContent());
    }
}
