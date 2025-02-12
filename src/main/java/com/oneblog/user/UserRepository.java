package com.oneblog.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByNickname(String nickname);

	Optional<User> findByGoogleUserId(String googleUserId);

	Optional<User> findByEmail(String email);

	@Query("select a.user from Article a where a.articleId = ?1")
	Optional<User> findUserByArticleId(Long articleId);

	boolean existsByNickname(String nickname);

	boolean existsByEmail(String email);
}
