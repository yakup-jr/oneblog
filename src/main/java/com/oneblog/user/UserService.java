package com.oneblog.user;

import com.oneblog.article.ArticleNotFoundException;
import com.oneblog.exceptions.ApiRequestException;
import com.oneblog.exceptions.PageNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

	User save(User user) throws ApiRequestException;

	Page<User> findAll(Integer page, Integer size) throws PageNotFoundException;

	boolean existsById(Long userId);

	User findById(Long id) throws UserNotFoundException;

	User findByNickname(String nickname) throws UserNotFoundException;

	User findByEmail(String email) throws UserNotFoundException;

	User findByArticleId(Long articleId) throws ArticleNotFoundException;

	boolean existsByNickname(String nickname);

	boolean existsByEmail(String email);

	void deleteById(Long id) throws UserNotFoundException;

}
