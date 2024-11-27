package com.oneblog.article;

import com.oneblog.exceptions.ApiRequestException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ArticleService {
	Article save(Article article) throws ApiRequestException;

	Article findByArticleId(Long id) throws ArticleNotFoundException;

	List<Article> findByUserId(Long userId) throws ArticleNotFoundException;

	Page<Article> findAll(Integer page, Integer size) throws ApiRequestException;

	void deleteByArticleId(Long id) throws ArticleNotFoundException;

	void deleteByUserId(Long userId) throws ArticleNotFoundException;
}
