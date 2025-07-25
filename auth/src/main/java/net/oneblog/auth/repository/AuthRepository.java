package net.oneblog.auth.repository;

import net.oneblog.auth.entity.AuthEntity;
import net.oneblog.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<AuthEntity, Long> {

    @Query("select a from AuthEntity a where a.userEntity.email = :email")
    Optional<AuthEntity> findByEmail(String email);

    /**
     * Find by google user id optional.
     *
     * @param googleUserId the google user id
     * @return the optional
     */
    Optional<UserEntity> findByGoogleUserId(String googleUserId);

}
