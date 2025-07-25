package net.oneblog.article.service;

import net.oneblog.api.interfaces.ArticleDomain;
import net.oneblog.article.dto.ArticleCreateDto;
import net.oneblog.article.dto.ArticleDto;
import net.oneblog.article.exception.ArticleNotFoundException;
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
     * @throws ApiRequestException the api request exception
     */
    ArticleDto save(ArticleCreateDto articleDomain)
        throws ApiRequestException, LabelNotFoundException;

    /**
     * Find by article id article.
     *
     * @param id the id
     * @return the article
     */
    ArticleDto findByArticleId(Long id);

    /**
     * Find by user id list.
     *
     * @param userId the user id
     * @return the list
     */
    List<ArticleDto> findByUserId(Long userId);

    /**
     * Find all page.
     *
     * @param page the page
     * @param size the size
     * @return the page
     */
    Page<ArticleDto> findAll(Integer page, Integer size);

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
