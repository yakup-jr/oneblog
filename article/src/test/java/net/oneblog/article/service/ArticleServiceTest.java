package net.oneblog.article.service;

import net.oneblog.api.interfaces.VoteType;
import net.oneblog.article.entity.ArticleEntity;
import net.oneblog.article.entity.LabelEntity;
import net.oneblog.article.exception.ArticleNotFoundException;
import net.oneblog.article.mapper.ArticleMapper;
import net.oneblog.article.models.ArticleCreateModel;
import net.oneblog.article.models.ArticleModel;
import net.oneblog.article.models.LabelModel;
import net.oneblog.article.repository.ArticleRepository;
import net.oneblog.sharedexceptions.ApiRequestException;
import net.oneblog.user.entity.UserEntity;
import net.oneblog.user.exceptions.UserNotFoundException;
import net.oneblog.user.mappers.UserMapper;
import net.oneblog.user.service.UserService;
import net.oneblog.user.service.UserValidationService;
import net.oneblog.validationapi.models.ValidatedUserModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private UserService userService;
    @Mock
    private UserValidationService userValidationService;
    @Mock
    private LabelService labelService;
    @Mock
    private ArticleMapper articleMapper;
    @Mock
    private UserMapper userMapper; // ide doesn't see the dependence
    @Mock
    private VoteManagerService voteManagerService;

    @InjectMocks
    private ArticleServiceImpl articleService;

    @Test
    void save_Success() {
        ValidatedUserModel userModel = ValidatedUserModel.builder().userId(1L).build();
        LabelModel labelModel = LabelModel.builder().labelId(2L).build();
        List<LabelModel> labelModels = List.of(labelModel);

        ArticleCreateModel createDto =
            ArticleCreateModel.builder().user(userModel).labels(labelModels).build();
        ArticleModel savedDto = ArticleModel.builder().articleId(1L).user(userModel).build();

        UserEntity fetchedUser = UserEntity.builder().userId(1L).build();
        LabelEntity fetchedLabel = LabelEntity.builder().labelId(2L).build();

        ArticleEntity articleFromMapper = ArticleEntity.builder()
            .labelEntities(List.of(
                LabelEntity.builder().labelId(2L).build())) // Pass a list with a mock entity
            .userEntity(UserEntity.builder().userId(1L).build())
            .build();

        ArticleEntity savedArticle = ArticleEntity.builder().articleId(1L).build();

        when(articleMapper.map(createDto)).thenReturn(articleFromMapper);

        when(userService.findById(userModel.userId())).thenReturn(userModel);
        when(userMapper.map(userModel)).thenReturn(fetchedUser);

        when(labelService.findById(labelModel.getLabelId())).thenReturn(
            fetchedLabel); // Use the variable with the ID
        when(articleRepository.save(any(ArticleEntity.class))).thenReturn(savedArticle);

        when(articleMapper.map(savedArticle)).thenReturn(savedDto);

        ArticleModel result = articleService.save(createDto);

        assertNotNull(result);
        assertEquals(savedDto.getArticleId(), result.getArticleId());

        verify(userService).findById(userModel.userId());
        verify(userMapper).map(userModel);
        verify(labelService).findById(labelModel.getLabelId());
        verify(articleRepository).save(any(ArticleEntity.class));
    }

    @Test
    void findByArticleId_ShouldIncludeLikeAndDislikeCount() {
        ArticleEntity entity = ArticleEntity.builder().articleId(1L).build();
        ArticleModel dto = ArticleModel.builder().articleId(1L).build();

        when(articleRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(articleMapper.map(entity)).thenReturn(dto);
        when(voteManagerService.countByArticleIdAndVoteType(1L, VoteType.LIKE)).thenReturn(5L);
        when(voteManagerService.countByArticleIdAndVoteType(1L, VoteType.DISLIKE)).thenReturn(2L);

        ArticleModel result = articleService.findByArticleId(1L);

        assertNotNull(result);
        verify(voteManagerService).countByArticleIdAndVoteType(1L, VoteType.LIKE);
        verify(voteManagerService).countByArticleIdAndVoteType(1L, VoteType.DISLIKE);
    }


    @Test
    void findByArticleId_Success() {
        ArticleEntity entity = new ArticleEntity();
        ArticleModel dto = new ArticleModel();

        when(articleRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(articleMapper.map(entity)).thenReturn(dto);

        ArticleModel result = articleService.findByArticleId(1L);

        assertNotNull(result);
    }

    @Test
    void findByArticleId_NotFound() {
        assertThrows(ArticleNotFoundException.class,
            () -> articleService.findByArticleId(1L));
    }

    @Test
    void findByUserId_ShouldIncludeLikeAndDislikeCount() {
        ArticleEntity entity = ArticleEntity.builder().articleId(1L).build();
        List<ArticleEntity> entities = List.of(entity);
        ArticleModel dto = ArticleModel.builder().articleId(1L).build();

        when(articleRepository.findByUserId(1L)).thenReturn(entities);
        when(articleMapper.map(entity)).thenReturn(dto);
        when(voteManagerService.countByArticleIdAndVoteType(1L, VoteType.LIKE)).thenReturn(3L);
        when(voteManagerService.countByArticleIdAndVoteType(1L, VoteType.DISLIKE)).thenReturn(1L);

        List<ArticleModel> result = articleService.findByUserId(1L);

        assertEquals(1, result.size());
        assertEquals(3L, result.getFirst().getLikes());
        assertEquals(1L, result.getFirst().getDislikes());
        verify(voteManagerService).countByArticleIdAndVoteType(1L, VoteType.LIKE);
        verify(voteManagerService).countByArticleIdAndVoteType(1L, VoteType.DISLIKE);
    }

    @Test
    void findByUserId_NotFound() {
        when(articleRepository.findByUserId(1L)).thenReturn(List.of());

        assertThrows(ArticleNotFoundException.class,
            () -> articleService.findByUserId(1L));
    }

    @Test
    void findAll_ShouldIncludeLikeAndDislikeCount() {
        ArticleEntity entity = ArticleEntity.builder().articleId(1L).build();
        Page<ArticleEntity> entityPage = new PageImpl<>(List.of(entity));
        ArticleModel dto = ArticleModel.builder().articleId(1L).build();

        when(articleRepository.findAll(any(PageRequest.class))).thenReturn(entityPage);
        when(articleMapper.map(entity)).thenReturn(dto);
        when(voteManagerService.countByArticleIdAndVoteType(1L, VoteType.LIKE)).thenReturn(4L);
        when(voteManagerService.countByArticleIdAndVoteType(1L, VoteType.DISLIKE)).thenReturn(2L);

        Page<ArticleModel> result = articleService.findAll(0, 10);

        assertFalse(result.isEmpty());
        assertEquals(4L, result.getContent().getFirst().getLikes());
        assertEquals(2L, result.getContent().getFirst().getDislikes());
        verify(voteManagerService).countByArticleIdAndVoteType(1L, VoteType.LIKE);
        verify(voteManagerService).countByArticleIdAndVoteType(1L, VoteType.DISLIKE);
    }

    @Test
    void findAll_EmptyPage() {
        Page<ArticleEntity> emptyPage = new PageImpl<>(List.of());

        when(articleRepository.findAll(any(PageRequest.class))).thenReturn(emptyPage);

        assertThrows(ApiRequestException.class,
            () -> articleService.findAll(0, 10));
    }

    @Test
    void deleteByArticleId_Success() {
        when(articleRepository.existsById(1L)).thenReturn(true);

        articleService.deleteByArticleId(1L);

        verify(articleRepository).deleteById(1L);
    }

    @Test
    void deleteByArticleId_NotFound() {
        when(articleRepository.existsById(1L)).thenReturn(false);

        assertThrows(ArticleNotFoundException.class,
            () -> articleService.deleteByArticleId(1L));
    }

    @Test
    void deleteByUserId_Success() {
        when(userValidationService.existsById(1L)).thenReturn(true);
        when(articleRepository.existsByUserId(1L)).thenReturn(true);

        articleService.deleteByUserId(1L);

        verify(articleRepository).deleteByUserId(1L);
    }

    @Test
    void deleteByUserId_UserNotFound() {
        when(userValidationService.existsById(1L)).thenReturn(false);

        assertThrows(UserNotFoundException.class,
            () -> articleService.deleteByUserId(1L));
    }

    @Test
    void deleteByUserId_ArticleNotFound() {
        when(userValidationService.existsById(1L)).thenReturn(true);
        when(articleRepository.existsByUserId(1L)).thenReturn(false);

        assertThrows(ArticleNotFoundException.class,
            () -> articleService.deleteByUserId(1L));
    }
}
