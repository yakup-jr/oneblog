package net.oneblog.article.service;

import net.oneblog.article.dto.ArticleCreateDto;
import net.oneblog.article.dto.ArticleDto;
import net.oneblog.article.entity.ArticleEntity;
import net.oneblog.article.exception.ArticleNotFoundException;
import net.oneblog.article.mapper.ArticleMapper;
import net.oneblog.article.repository.ArticleRepository;
import net.oneblog.sharedexceptions.ApiRequestException;
import net.oneblog.user.exceptions.UserNotFoundException;
import net.oneblog.user.mappers.UserMapper;
import net.oneblog.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Article service.
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final UserService userService;
    private final LabelService labelService;
    private final ArticleMapper articleMapper;
    private final UserMapper userMapper;

    /**
     * Instantiates a new Article service.
     *
     * @param articleRepository the article repository
     * @param userService       the user service
     * @param labelService      the label service
     * @param articleMapper     the article mapper
     * @param userMapper        the user mapper
     */
    public ArticleServiceImpl(ArticleRepository articleRepository, UserService userService,
                              LabelService labelService, ArticleMapper articleMapper,
                              UserMapper userMapper) {
        this.articleRepository = articleRepository;
        this.userService = userService;
        this.labelService = labelService;
        this.articleMapper = articleMapper;
        this.userMapper = userMapper;
    }

    @Override
    public ArticleDto save(ArticleCreateDto article) {
        ArticleEntity articleEntity = articleMapper.map(article);
        articleEntity.setLabelEntities(labelService.findLabels(articleEntity.getLabelEntities()));
        articleEntity.setUserEntity(
            userMapper.map(userService.findById(articleEntity.getUserEntity().getUserId())));
        return articleMapper.map(articleRepository.save(articleEntity));
    }

    @Override
    public ArticleDto findByArticleId(Long id) {
        return articleMapper.map(articleRepository.findById(id).orElseThrow(
            () -> new ArticleNotFoundException("Article with id: " + id + " not found")));
    }

    @Override
    public List<ArticleDto> findByUserId(Long userId)
        throws ArticleNotFoundException {
        List<ArticleEntity> articleEntities = articleRepository.findByUserId(userId);
        if (articleEntities.isEmpty()) {
            throw new ArticleNotFoundException("Article with user id: " + userId + " not found");
        }
        return articleEntities.stream().map(articleMapper::map).collect(Collectors.toList());
    }

    @Override
    public Page<ArticleDto> findAll(Integer page, Integer size) {
        try {
            Pageable pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Page<ArticleDto> pageContent =
                articleRepository.findAll(pageRequest).map(articleMapper::map);
            if (pageContent.isEmpty()) {
                throw new ApiRequestException(
                    "Page " + page + " of size " + size + " doesn't exist");
            }
            return pageContent;
        } catch (IllegalArgumentException e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

    @Override
    public void deleteByArticleId(Long id) {
        if (!articleRepository.existsById(id)) {
            throw new ArticleNotFoundException("Article with id: " + id + " not found");
        }
        articleRepository.deleteById(id);
    }

    @Override
    public void deleteByUserId(Long userId) {
        if (!userService.existsById(userId)) {
            throw new UserNotFoundException("User with id: " + userId + " not found");
        }
        if (!articleRepository.existsByUserId(userId)) {
            throw new ArticleNotFoundException("Articles with user id: " + userId + " not found");
        }
        articleRepository.deleteByUserId(userId);
    }
}
