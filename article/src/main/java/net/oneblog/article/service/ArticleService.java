package net.oneblog.article.service;

import net.oneblog.article.models.ArticleModel;
import net.oneblog.article.models.ArticleCreateModel;
import net.oneblog.article.exception.LabelNotFoundException;
import net.oneblog.sharedexceptions.ApiRequestException;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * The interface Article service.
 */
public interface ArticleService {
    /**
     * Save article.
     *
     * @param articleDomain the article
     * @return the article
     * @throws ApiRequestException    the api request exception
     * @throws LabelNotFoundException the label not found exception
     */
    ArticleModel save(ArticleCreateModel articleDomain)
        throws ApiRequestException, LabelNotFoundException;

    /**
     * Find by article id article.
     *
     * @param id the id
     * @return the article
     */
    ArticleModel findByArticleId(Long id);

    /**
     * Find by user id list.
     *
     * @param userId the user id
     * @return the list
     */
    List<ArticleModel> findByUserId(Long userId);

    /**
     * Find all page.
     *
     * @param page the page
     * @param size the size
     * @return the page
     */
    Page<ArticleModel> findAll(Integer page, Integer size);

    /**
     * Delete by article id.
     *
     * @param id the id
     */
    void deleteByArticleId(Long id);

    /**
     * Delete by user id.
     *
     * @param userId the user id
     */
    void deleteByUserId(Long userId);
}
