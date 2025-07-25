package net.oneblog.article.service;

import net.oneblog.article.dto.ArticleCreateDto;
import net.oneblog.article.dto.ArticleDto;
import net.oneblog.article.entity.ArticleEntity;
import net.oneblog.article.exception.ArticleNotFoundException;
import net.oneblog.article.mapper.ArticleMapper;
import net.oneblog.article.repository.ArticleRepository;
import net.oneblog.sharedexceptions.ApiRequestException;
import net.oneblog.user.dto.UserDto;
import net.oneblog.user.entity.UserEntity;
import net.oneblog.user.exceptions.UserNotFoundException;
import net.oneblog.user.mappers.UserMapper;
import net.oneblog.user.service.UserService;
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

    @InjectMocks
    private ArticleServiceImpl articleService;

    @Test
    void save_Success() {
        ArticleCreateDto createDto = new ArticleCreateDto();

        UserEntity userEntity = UserEntity.builder().userId(1L).build();
        ArticleEntity entity = ArticleEntity.builder()
            .userEntity(userEntity)
            .labelEntities(List.of())
            .build();

        ArticleDto savedDto = new ArticleDto();
        UserDto userDto = UserDto.builder().userId(1L).build();

        when(articleMapper.map(createDto)).thenReturn(entity);
        when(labelService.findLabels(any())).thenReturn(List.of());
        when(userService.findById(1L)).thenReturn(userDto);
        when(userMapper.map(userDto)).thenReturn(userEntity);
        when(articleRepository.save(entity)).thenReturn(entity);
        when(articleMapper.map(entity)).thenReturn(savedDto);

        ArticleDto result = articleService.save(createDto);

        assertNotNull(result);
        verify(articleRepository).save(entity);
    }


    @Test
    void findByArticleId_Success() {
        ArticleEntity entity = new ArticleEntity();
        ArticleDto dto = new ArticleDto();

        when(articleRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(articleMapper.map(entity)).thenReturn(dto);

        ArticleDto result = articleService.findByArticleId(1L);

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
        ArticleDto dto = new ArticleDto();

        when(articleRepository.findByUserId(1L)).thenReturn(entities);
        when(articleMapper.map(any(ArticleEntity.class))).thenReturn(dto);

        List<ArticleDto> result = articleService.findByUserId(1L);

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
        ArticleDto dto = new ArticleDto();

        when(articleRepository.findAll(any(PageRequest.class))).thenReturn(entityPage);
        when(articleMapper.map(any(ArticleEntity.class))).thenReturn(dto);

        Page<ArticleDto> result = articleService.findAll(0, 10);

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
