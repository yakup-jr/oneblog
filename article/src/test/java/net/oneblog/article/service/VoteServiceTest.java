package net.oneblog.article.service;

import net.oneblog.article.entity.ArticleEntity;
import net.oneblog.article.entity.VoteEntity;
import net.oneblog.article.exception.VoteAlreadyExistsException;
import net.oneblog.article.exception.VoteNotFoundException;
import net.oneblog.article.mapper.VoteMapper;
import net.oneblog.article.models.ArticleModel;
import net.oneblog.article.models.VoteCreateModel;
import net.oneblog.article.models.VoteModel;
import net.oneblog.article.repository.VoteRepository;
import net.oneblog.user.entity.UserEntity;
import net.oneblog.user.service.UserService;
import net.oneblog.validationapi.models.ValidatedUserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoteServiceTest {

    @InjectMocks
    private VoteServiceImpl voteService;

    @Mock
    private VoteRepository voteRepository;
    @Mock
    private VoteMapper mapper;
    @Mock
    private ArticleService articleService;
    @Mock
    private UserService userService;

    private VoteEntity voteEntity;
    private VoteModel voteModel;
    private VoteCreateModel voteCreateModel;

    @BeforeEach
    void setUp() {
        UserEntity userEntity = UserEntity.builder().userId(1L).build();
        ArticleEntity articleEntity = ArticleEntity.builder().articleId(1L).build();

        voteEntity = VoteEntity.builder()
            .voteId(1L)
            .article(articleEntity)
            .user(userEntity)
            .build();

        voteModel = VoteModel.builder().voteId(1L).build();

        voteCreateModel = VoteCreateModel.builder()
            .articleId(1L)
            .userId(1L)
            .build();
    }


    @Test
    void save_ShouldSaveVote() {
        ValidatedUserModel validatedUserModel = ValidatedUserModel.builder().userId(1L).build();
        ArticleModel articleModel = ArticleModel.builder().articleId(1L).build();

        when(voteRepository.existsByArticleIdAndUserId(1L, 1L)).thenReturn(false);
        when(userService.getReferenceByUserId(voteCreateModel.getUserId())).thenReturn(
            validatedUserModel);
        when(articleService.getReferenceByArticleId(voteCreateModel.getArticleId())).thenReturn(
            articleModel);
        when(mapper.map(any(VoteModel.class))).thenReturn(voteEntity);

        voteService.save(voteCreateModel);

        verify(voteRepository).save(voteEntity);
        verify(mapper).map(any(VoteModel.class));
        verify(userService).getReferenceByUserId(1L);
        verify(articleService).getReferenceByArticleId(1L);
    }

    @Test
    void save_ShouldThrowException_WhenVoteAlreadyExists() {
        when(voteRepository.existsByArticleIdAndUserId(1L, 1L)).thenReturn(true);

        assertThrows(VoteAlreadyExistsException.class, () -> voteService.save(voteCreateModel));

        verify(voteRepository).existsByArticleIdAndUserId(1L, 1L);
        verify(voteRepository, never()).save(any());
    }


    @Test
    void findByVoteId_ShouldReturnVoteModel_WhenVoteExists() {
        when(voteRepository.findById(1L)).thenReturn(Optional.of(voteEntity));
        when(mapper.map(voteEntity)).thenReturn(voteModel);

        VoteModel result = voteService.findByVoteId(1L);

        assertEquals(voteModel, result);
        verify(voteRepository).findById(1L);
        verify(mapper).map(voteEntity);
    }

    @Test
    void findByVoteId_ShouldThrowException_WhenVoteNotFound() {
        when(voteRepository.findById(1L)).thenReturn(Optional.empty());

        VoteNotFoundException exception =
            assertThrows(VoteNotFoundException.class, () -> voteService.findByVoteId(1L));

        assertEquals("vote with id 1 not found", exception.getMessage());
        verify(voteRepository).findById(1L);
        verifyNoInteractions(mapper);
    }

    @Test
    void countByArticleId_ShouldReturnCount() {
        when(voteRepository.countByArticleId(1L)).thenReturn(5L);

        Long result = voteService.countByArticleId(1L);

        assertEquals(5L, result);
        verify(voteRepository).countByArticleId(1L);
    }

    @Test
    void countByUserId_ShouldReturnCount() {
        when(voteRepository.coundByUserId(1L)).thenReturn(3L);

        Long result = voteService.countByUserId(1L);

        assertEquals(3L, result);
        verify(voteRepository).coundByUserId(1L);
    }

    @Test
    void findByUserId_ShouldReturnVoteModels() {
        Set<VoteEntity> entities = Set.of(voteEntity);
        Set<VoteModel> models = Set.of(voteModel);

        when(voteRepository.findByUserId(1L)).thenReturn(entities);
        when(mapper.map(voteEntity)).thenReturn(voteModel);

        Set<VoteModel> result = voteService.findByUserId(1L);

        assertEquals(models, result);
        verify(voteRepository).findByUserId(1L);
        verify(mapper).map(voteEntity);
    }

    @Test
    void findByUserId_ShouldReturnEmptySet_WhenNoVotesFound() {
        when(voteRepository.findByUserId(1L)).thenReturn(Set.of());

        Set<VoteModel> result = voteService.findByUserId(1L);

        assertTrue(result.isEmpty());
        verify(voteRepository).findByUserId(1L);
        verifyNoInteractions(mapper);
    }

    @Test
    void findByArticleId_ShouldReturnVoteModels() {
        Set<VoteEntity> entities = Set.of(voteEntity);
        Set<VoteModel> models = Set.of(voteModel);

        when(voteRepository.findByArticleId(1L)).thenReturn(entities);
        when(mapper.map(voteEntity)).thenReturn(voteModel);

        Set<VoteModel> result = voteService.findByArticleId(1L);

        assertEquals(models, result);
        verify(voteRepository).findByArticleId(1L);
        verify(mapper).map(voteEntity);
    }

    @Test
    void findByArticleId_ShouldReturnEmptySet_WhenNoVotesFound() {
        when(voteRepository.findByArticleId(1L)).thenReturn(Set.of());

        Set<VoteModel> result = voteService.findByArticleId(1L);

        assertTrue(result.isEmpty());
        verify(voteRepository).findByArticleId(1L);
        verifyNoInteractions(mapper);
    }

    @Test
    void deleteByVoteId_ShouldDeleteVote() {
        voteService.deleteByVoteId(1L);

        verify(voteRepository).deleteById(1L);
    }

    @Test
    void deleteByUserId_ShouldDeleteVotesByUserId() {
        voteService.deleteByUserId(1L);

        verify(voteRepository).deleteByUserId(1L);
    }

    @Test
    void deleteByArticleId_ShouldDeleteVotesByArticleId() {
        voteService.deleteByArticleId(1L);

        verify(voteRepository).deleteByArticleId(1L);
    }
}