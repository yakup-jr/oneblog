package net.oneblog.email.repository;

import net.oneblog.email.entity.EmailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The interface Email verification repository.
 */
@Repository
public interface EmailVerificationRepository extends JpaRepository<EmailEntity, Long> {

    /**
     * Exists by email and code not expired boolean.
     *
     * @param email the email
     * @return the boolean
     */
    @Query("select count(emailVerification) > 0 from EmailEntity emailVerification where " +
        "emailVerification.email = :email and emailVerification.expiresAt > CURRENT_TIMESTAMP")
    boolean existsByEmailAndCodeNotExpired(String email);

    /**
     * Find by email optional.
     *
     * @param email the email
     * @return the optional
     */
    @Query("select e from EmailEntity e where e.email = :email")
    Optional<EmailEntity> findByEmail(String email);

}
