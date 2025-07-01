package com.oneblog.user;

import com.oneblog.article.ArticleNotFoundException;
import com.oneblog.exceptions.ApiRequestException;
import com.oneblog.exceptions.PageNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * The interface User service.
 */
public interface UserService extends UserDetailsService {

	/**
	 * Save user.
	 *
	 * @param user the user
	 * @return the user
	 * @throws ApiRequestException the api request exception
	 */
	User save(User user) throws ApiRequestException;

	/**
	 * Find all page.
	 *
	 * @param page the page
	 * @param size the size
	 * @return the page
	 * @throws PageNotFoundException the page not found exception
	 */
	Page<User> findAll(Integer page, Integer size) throws PageNotFoundException;

	/**
	 * Exists by id boolean.
	 *
	 * @param userId the user id
	 * @return the boolean
	 */
	boolean existsById(Long userId);

	/**
	 * Find by id user.
	 *
	 * @param id the id
	 * @return the user
	 * @throws UserNotFoundException the user not found exception
	 */
	User findById(Long id) throws UserNotFoundException;

	/**
	 * Find by nickname user.
	 *
	 * @param nickname the nickname
	 * @return the user
	 * @throws UserNotFoundException the user not found exception
	 */
	User findByNickname(String nickname) throws UserNotFoundException;

	/**
	 * Find by email user.
	 *
	 * @param email the email
	 * @return the user
	 * @throws UserNotFoundException the user not found exception
	 */
	User findByEmail(String email) throws UserNotFoundException;

	/**
	 * Find by article id user.
	 *
	 * @param articleId the article id
	 * @return the user
	 * @throws ArticleNotFoundException the article not found exception
	 */
	User findByArticleId(Long articleId) throws ArticleNotFoundException;

	/**
	 * Exists by nickname boolean.
	 *
	 * @param nickname the nickname
	 * @return the boolean
	 */
	boolean existsByNickname(String nickname);

	/**
	 * Exists by email boolean.
	 *
	 * @param email the email
	 * @return the boolean
	 */
	boolean existsByEmail(String email);

	/**
	 * Delete by id.
	 *
	 * @param id the id
	 * @throws UserNotFoundException the user not found exception
	 */
	void deleteById(Long id) throws UserNotFoundException;

}
