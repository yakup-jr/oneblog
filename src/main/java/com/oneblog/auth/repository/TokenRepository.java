package com.oneblog.auth.repository;

import com.oneblog.auth.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Token repository.
 */
@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

	/**
	 * Find by access token optional.
	 *
	 * @param accessToken the access token
	 * @return the optional
	 */
	Optional<Token> findByAccessToken(String accessToken);

	/**
	 * Find by refresh token optional.
	 *
	 * @param refreshToken the refresh token
	 * @return the optional
	 */
	Optional<Token> findByRefreshToken(String refreshToken);

	/**
	 * Find all access token by user list.
	 *
	 * @param userId the user id
	 * @return the list
	 */
	@Query("""
		select t from Token t where t.tokenId = :userId
		""")
	List<Token> findAllAccessTokenByUser(Long userId);

}
