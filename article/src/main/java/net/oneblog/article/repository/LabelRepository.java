package net.oneblog.article.repository;

import net.oneblog.api.interfaces.LabelName;
import net.oneblog.article.entity.LabelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The interface Label repository.
 */
@Repository
public interface LabelRepository
    extends JpaRepository<LabelEntity, Long>, PagingAndSortingRepository<LabelEntity, Long> {

    /**
     * Find by name optional.
     *
     * @param name the name
     * @return the optional
     */
    Optional<LabelEntity> findByName(LabelName name);

}
