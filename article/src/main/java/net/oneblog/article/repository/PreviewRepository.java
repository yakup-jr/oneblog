package net.oneblog.article.repository;

import net.oneblog.article.entity.PreviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The interface Preview repository.
 */
public interface PreviewRepository extends JpaRepository<PreviewEntity, Long> {



}
