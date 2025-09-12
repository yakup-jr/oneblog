package net.oneblog.article.service;

import net.oneblog.api.interfaces.VoteType;
import net.oneblog.article.entity.ArticleEntity;
import net.oneblog.article.entity.VoteEntity;
import net.oneblog.article.exception.VoteAlreadyExistsException;
import net.oneblog.article.exception.VoteNotFoundException;
import net.oneblog.article.models.VoteCreateModel;
import net.oneblog.article.models.VoteModel;
import net.oneblog.article.repository.ArticleRepository;
import net.oneblog.article.repository.VoteRepository;
import net.oneblog.sharedconfig.test.IntegrationTest;
import net.oneblog.user.entity.UserEntity;
import net.oneblog.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
class VoteServiceIntegrationTest {
    @Autowired
    private VoteService voteService;
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArticleRepository articleRepository;

    private UserEntity testUser;
    private ArticleEntity testArticle;

    @BeforeEach
    void setUp() {
        testUser = userRepository.findById(1L).orElseThrow();
        testArticle = articleRepository.findById(3L).orElseThrow();
    }

    @Test
    void save_ShouldCreateVoteInDatabase() {
        VoteCreateModel voteCreateModel = VoteCreateModel.builder()
            .articleId(4L)
            .userId(5L)
            .voteType(VoteType.LIKE)
            .build();

        voteService.save(voteCreateModel);

        Set<VoteEntity> votes = voteRepository.findByUserId(testUser.getUserId());
        assertFalse(votes.isEmpty());
    }

    @Test
    void save_ShouldThrowException_WhenVoteAlreadyExists() {
        VoteCreateModel voteCreateModel = VoteCreateModel.builder()
            .articleId(1L)
            .userId(1L)
            .voteType(VoteType.LIKE)
            .build();

        assertThrows(VoteAlreadyExistsException.class, () -> voteService.save(voteCreateModel));
    }

    @Test
    void findByVoteId_ShouldReturnVote_WhenVoteExists() {
        VoteModel result = voteService.findByVoteId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getVoteId());
    }

    @Test
    void findByVoteId_ShouldThrowException_WhenVoteNotFound() {
        assertThrows(VoteNotFoundException.class, () -> voteService.findByVoteId(999L));
    }

    @Test
    void countByArticleId_ShouldReturnCorrectCount() {
        Long count = voteService.countByArticleId(testArticle.getArticleId());
        assertEquals(1, count);
    }

    @Test
    void countByUserId_ShouldReturnCorrectCount() {
        VoteEntity vote1 = VoteEntity.builder()
            .article(testArticle)
            .user(testUser)
            .voteType(VoteType.LIKE)
            .build();
        voteRepository.save(vote1);

        Long count = voteService.countByUserId(testUser.getUserId());

        assertTrue(count >= 1);
    }

    @Test
    void findByUserId_ShouldReturnUserVotes() {
        Set<VoteModel> votes = voteService.findByUserId(testUser.getUserId());
        assertEquals(2, votes.size());
    }

    @Test
    void findByArticleId_ShouldReturnArticleVotes() {
        Set<VoteModel> votes = voteService.findByArticleId(testArticle.getArticleId());
        assertEquals(1, votes.size());
    }

    @Test
    void deleteByVoteId_ShouldRemoveVote() {
        voteService.deleteByVoteId(1L);

        assertFalse(voteRepository.existsById(1L));
    }

    @Test
    void deleteByUserId_ShouldRemoveAllUserVotes() {
        voteService.deleteByUserId(testUser.getUserId());

        Set<VoteEntity> votes = voteRepository.findByUserId(testUser.getUserId());
        assertTrue(votes.isEmpty());
    }

    @Test
    void deleteByArticleId_ShouldRemoveAllArticleVotes() {
        voteService.deleteByArticleId(testArticle.getArticleId());

        Set<VoteEntity> votes = voteRepository.findByArticleId(testArticle.getArticleId());
        assertTrue(votes.isEmpty());
    }
}
