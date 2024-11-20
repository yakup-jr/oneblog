package com.oneblog.article;

import com.oneblog.article.label.LabelNotFoundException;
import com.oneblog.exceptions.ApiRequestException;
import com.oneblog.user.UserNotFoundException;

import java.util.List;

public interface ArticleService {
	Article save(Article article) throws ApiRequestException, LabelNotFoundException, UserNotFoundException;

	Article findByArticleId(Long id) throws ArticleNotFoundException;

	List<Article> findByUserId(Long userId) throws ArticleNotFoundException;

	void deleteByArticleId(Long id) throws ArticleNotFoundException;

	void deleteByUserId(Long userId) throws ArticleNotFoundException;
}
