package com.oneblog.user;

import com.oneblog.article.ArticleNotFoundException;
import com.oneblog.exceptions.ApiRequestException;
import com.oneblog.exceptions.PageNotFoundException;
import org.springframework.data.domain.Page;

public interface UserService {

	User save(User user) throws ApiRequestException;

	boolean existsById(Long userId);

	Page<User> findAll(Integer page, Integer size) throws PageNotFoundException;

	User findById(Long id) throws UserNotFoundException;

	User findByNickname(String nickname) throws UserNotFoundException;

	User findByEmail(String email) throws UserNotFoundException;

	User findByArticleId(Long articleId) throws ArticleNotFoundException;

	void deleteById(Long id) throws UserNotFoundException;

}
