package com.oneblog.auth.repository;

import com.oneblog.auth.entity.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The interface Email verification repository.
 */
@Repository
public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {

	/**
	 * Exists by email and code not expired boolean.
	 *
	 * @param email the email
	 * @return the boolean
	 */
	@Query("select count(emailVerification) > 0 from EmailVerification emailVerification where emailVerification.user" +
	       ".email = :email and emailVerification.expiresAt > CURRENT_TIMESTAMP")
	boolean existsByEmailAndCodeNotExpired(String email);

	/**
	 * Find by email optional.
	 *
	 * @param email the email
	 * @return the optional
	 */
	@Query("select e from EmailVerification e where e.user.email = :email")
	Optional<EmailVerification> findByEmail(String email);

}
