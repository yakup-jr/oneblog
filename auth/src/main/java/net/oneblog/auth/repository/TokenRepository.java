package net.oneblog.auth.repository;

import net.oneblog.auth.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Token repository.
 */
@Repository
public interface TokenRepository extends JpaRepository<TokenEntity, Long> {

	/**
	 * Find by access tokenEntity optional.
	 *
	 * @param accessToken the access tokenEntity
	 * @return the optional
	 */
	Optional<TokenEntity> findByAccessToken(String accessToken);

	/**
	 * Find by refresh tokenEntity optional.
	 *
	 * @param refreshToken the refresh tokenEntity
	 * @return the optional
	 */
	Optional<TokenEntity> findByRefreshToken(String refreshToken);

	/**
	 * Find all access tokenEntity by user list.
	 *
	 * @param userId the user id
	 * @return the list
	 */
	@Query("""
		select t from TokenEntity t where t.tokenId = :userId
		""")
	List<TokenEntity> findAllAccessTokenByUser(Long userId);

}
