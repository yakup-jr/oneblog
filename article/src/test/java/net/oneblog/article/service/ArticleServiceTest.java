package net.oneblog.article.service;

import net.oneblog.article.entity.ArticleEntity;
import net.oneblog.article.exception.ArticleNotFoundException;
import net.oneblog.article.mapper.ArticleMapper;
import net.oneblog.article.models.ArticleCreateModel;
import net.oneblog.article.models.ArticleModel;
import net.oneblog.article.repository.ArticleRepository;
import net.oneblog.sharedexceptions.ApiRequestException;
import net.oneblog.user.entity.UserEntity;
import net.oneblog.user.exceptions.UserNotFoundException;
import net.oneblog.user.mappers.UserMapper;
import net.oneblog.user.service.UserService;
import net.oneblog.validationapi.mappers.ValidatedUserModelMapperImpl;
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
    private LabelService labelService;
    @Mock
    private ArticleMapper articleMapper;
    @Mock
    private UserMapper userMapper;
    @Mock
    private ValidatedUserModelMapperImpl validatedUserModelMapper;

    @InjectMocks
    private ArticleServiceImpl articleService;

    @Test
    void save_Success() {
        ValidatedUserModel userModel = ValidatedUserModel.builder().userId(1L).build();
        ArticleEntity notSaved =
            ArticleEntity.builder().userEntity(UserEntity.builder().userId(1L).build()).build();
        ArticleCreateModel createDto =
            ArticleCreateModel.builder().user(userModel).labels(List.of()).build();
        ArticleEntity saved =
            ArticleEntity.builder().articleId(1L).userEntity(UserEntity.builder().userId(1L).build()).build();
        ArticleModel savedDto = ArticleModel.builder().articleId(1L).user(userModel).build();

        when(articleMapper.map(createDto)).thenReturn(notSaved);
        when(labelService.findLabels(any())).thenReturn(List.of());
        when(userService.findById(1L)).thenReturn(userModel);
        when(articleRepository.save(notSaved)).thenReturn(saved);
        when(articleMapper.map(saved)).thenReturn(savedDto);

        ArticleModel result = articleService.save(createDto);

        assertNotNull(result);
        verify(articleRepository).save(notSaved);
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
    void findByUserId_Success() {
        List<ArticleEntity> entities = List.of(new ArticleEntity());
        ArticleModel dto = new ArticleModel();

        when(articleRepository.findByUserId(1L)).thenReturn(entities);
        when(articleMapper.map(any(ArticleEntity.class))).thenReturn(dto);

        List<ArticleModel> result = articleService.findByUserId(1L);

        assertEquals(1, result.size());
    }

    @Test
    void findByUserId_NotFound() {
        when(articleRepository.findByUserId(1L)).thenReturn(List.of());

        assertThrows(ArticleNotFoundException.class,
            () -> articleService.findByUserId(1L));
    }

    @Test
    void findAll_Success() {
        Page<ArticleEntity> entityPage = new PageImpl<>(List.of(new ArticleEntity()));
        ArticleModel dto = new ArticleModel();

        when(articleRepository.findAll(any(PageRequest.class))).thenReturn(entityPage);
        when(articleMapper.map(any(ArticleEntity.class))).thenReturn(dto);

        Page<ArticleModel> result = articleService.findAll(0, 10);

        assertFalse(result.isEmpty());
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
        when(userService.existsById(1L)).thenReturn(true);
        when(articleRepository.existsByUserId(1L)).thenReturn(true);

        articleService.deleteByUserId(1L);

        verify(articleRepository).deleteByUserId(1L);
    }

    @Test
    void deleteByUserId_UserNotFound() {
        when(userService.existsById(1L)).thenReturn(false);

        assertThrows(UserNotFoundException.class,
            () -> articleService.deleteByUserId(1L));
    }

    @Test
    void deleteByUserId_ArticleNotFound() {
        when(userService.existsById(1L)).thenReturn(true);
        when(articleRepository.existsByUserId(1L)).thenReturn(false);

        assertThrows(ArticleNotFoundException.class,
            () -> articleService.deleteByUserId(1L));
    }
}
