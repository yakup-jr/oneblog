package net.oneblog.auth.repository;

import net.oneblog.api.interfaces.RoleNameDomain;
import net.oneblog.auth.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The interface Role repository.
 */
@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    /**
     * Find by name optional.
     *
     * @param name the name
     * @return the optional
     */
    Optional<RoleEntity> findByName(RoleNameDomain name);

}
