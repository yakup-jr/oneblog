package com.oneblog.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

	Optional<Token> findByAccessToken(String accessToken);

	Optional<Token> findByRefreshToken(String refreshToken);

	@Query("""
		select t from Token t where t.tokenId = :userId
		""")
	List<Token> findAllAccessTokenByUser(Long userId);

}
