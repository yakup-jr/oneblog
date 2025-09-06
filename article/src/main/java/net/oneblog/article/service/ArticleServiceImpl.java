package net.oneblog.article.service;

import lombok.AllArgsConstructor;
import net.oneblog.article.entity.ArticleEntity;
import net.oneblog.article.exception.ArticleNotFoundException;
import net.oneblog.article.mapper.ArticleMapper;
import net.oneblog.article.models.ArticleCreateModel;
import net.oneblog.article.models.ArticleModel;
import net.oneblog.article.repository.ArticleRepository;
import net.oneblog.sharedexceptions.ApiRequestException;
import net.oneblog.user.exceptions.UserNotFoundException;
import net.oneblog.user.mappers.UserMapper;
import net.oneblog.user.service.UserService;
import net.oneblog.user.service.UserValidationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Article service.
 */
@Service
@AllArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final UserService userService;
    private final UserValidationService userValidationService;
    private final LabelService labelService;
    private final ArticleMapper articleMapper;
    private final UserMapper userMapper;

    @Override
    public ArticleModel save(ArticleCreateModel article) {
        ArticleEntity articleEntity = articleMapper.map(article);
        articleEntity.setLabelEntities(labelService.findLabels(articleEntity.getLabelEntities()));
        articleEntity.setUserEntity(
            userMapper.map(userService.findById(articleEntity.getUserEntity().getUserId())));
        return articleMapper.map(articleRepository.save(articleEntity));
    }

    @Override
    public ArticleModel findByArticleId(Long id) {
        return articleMapper.map(articleRepository.findById(id).orElseThrow(
            () -> new ArticleNotFoundException("Article with id: " + id + " not found")));
    }

    @Override
    public List<ArticleModel> findByUserId(Long userId)
        throws ArticleNotFoundException {
        List<ArticleEntity> articleEntities = articleRepository.findByUserId(userId);
        if (articleEntities.isEmpty()) {
            throw new ArticleNotFoundException("Article with user id: " + userId + " not found");
        }
        return articleEntities.stream().map(articleMapper::map).toList();
    }

    @Override
    public Page<ArticleModel> findAll(Integer page, Integer size) {
        try {
            Pageable pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Page<ArticleModel> pageContent =
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
        if (!userValidationService.existsById(userId)) {
            throw new UserNotFoundException("User with id: " + userId + " not found");
        }
        if (!articleRepository.existsByUserId(userId)) {
            throw new ArticleNotFoundException("Articles with user id: " + userId + " not found");
        }
        articleRepository.deleteByUserId(userId);
    }
}
