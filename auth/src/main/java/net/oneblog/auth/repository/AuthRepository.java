package net.oneblog.auth.repository;

import net.oneblog.auth.entity.AuthEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The interface Auth repository.
 */
@Repository
public interface AuthRepository extends JpaRepository<AuthEntity, Long> {

    /**
     * Find by email optional.
     *
     * @param email the email
     * @return the optional
     */
    @Query("select a from AuthEntity a where a.userEntity.email = :email")
    Optional<AuthEntity> findByEmail(String email);

    /**
     * Find by google user id optional.
     *
     * @param googleUserId the google user id
     * @return the optional
     */
    Optional<AuthEntity> findByGoogleUserId(String googleUserId);

}
