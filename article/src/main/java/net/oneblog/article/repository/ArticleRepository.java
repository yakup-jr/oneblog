package net.oneblog.article.repository;

import net.oneblog.article.entity.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Article repository.
 */
@Repository
public interface ArticleRepository
    extends JpaRepository<ArticleEntity, Long>, PagingAndSortingRepository<ArticleEntity, Long> {

    /**
     * Find by user id list.
     *
     * @param userId the user id
     * @return the list
     */
    @Query(value = "select ar from ArticleEntity ar where ar.userEntity.userId = ?1")
    List<ArticleEntity> findByUserId(Long userId);

    /**
     * Exists by user id boolean.
     *
     * @param userId the user id
     * @return the boolean
     */
    @Query("select count(ar)>0 from ArticleEntity ar where ar.userEntity.userId = ?1")
    boolean existsByUserId(Long userId);

    /**
     * Delete by user id.
     *
     * @param userId the user id
     */
    @Modifying
    @Query("delete from ArticleEntity ar where ar.userEntity.userId = ?1")
    void deleteByUserId(Long userId);

}
