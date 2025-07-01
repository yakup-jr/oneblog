package com.oneblog.article;

import com.oneblog.exceptions.ApiRequestException;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * The interface Article service.
 */
public interface ArticleService {
	/**
	 * Save article.
	 *
	 * @param article the article
	 * @return the article
	 * @throws ApiRequestException the api request exception
	 */
	Article save(Article article) throws ApiRequestException;

	/**
	 * Find by article id article.
	 *
	 * @param id the id
	 * @return the article
	 * @throws ArticleNotFoundException the article not found exception
	 */
	Article findByArticleId(Long id) throws ArticleNotFoundException;

	/**
	 * Find by user id list.
	 *
	 * @param userId the user id
	 * @return the list
	 * @throws ArticleNotFoundException the article not found exception
	 */
	List<Article> findByUserId(Long userId) throws ArticleNotFoundException;

	/**
	 * Find all page.
	 *
	 * @param page the page
	 * @param size the size
	 * @return the page
	 * @throws ApiRequestException the api request exception
	 */
	Page<Article> findAll(Integer page, Integer size) throws ApiRequestException;

	/**
	 * Delete by article id.
	 *
	 * @param id the id
	 * @throws ArticleNotFoundException the article not found exception
	 */
	void deleteByArticleId(Long id) throws ArticleNotFoundException;

	/**
	 * Delete by user id.
	 *
	 * @param userId the user id
	 * @throws ArticleNotFoundException the article not found exception
	 */
	void deleteByUserId(Long userId) throws ArticleNotFoundException;
}
