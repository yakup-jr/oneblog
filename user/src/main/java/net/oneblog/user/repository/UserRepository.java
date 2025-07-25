package net.oneblog.user.repository;

import net.oneblog.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The interface User repository.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

	/**
	 * Find by nickname optional.
	 *
	 * @param nickname the nickname
	 * @return the optional
	 */
	Optional<UserEntity> findByNickname(String nickname);

	/**
	 * Find by email optional.
	 *
	 * @param email the email
	 * @return the optional
	 */
	Optional<UserEntity> findByEmail(String email);

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
