package com.oneblog.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The interface User repository.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	/**
	 * Find by nickname optional.
	 *
	 * @param nickname the nickname
	 * @return the optional
	 */
	Optional<User> findByNickname(String nickname);

	/**
	 * Find by google user id optional.
	 *
	 * @param googleUserId the google user id
	 * @return the optional
	 */
	Optional<User> findByGoogleUserId(String googleUserId);

	/**
	 * Find by email optional.
	 *
	 * @param email the email
	 * @return the optional
	 */
	Optional<User> findByEmail(String email);

	/**
	 * Find user by article id optional.
	 *
	 * @param articleId the article id
	 * @return the optional
	 */
	@Query("select a.user from Article a where a.articleId = ?1")
	Optional<User> findUserByArticleId(Long articleId);

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
}
