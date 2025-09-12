package net.oneblog.article.repository;

import net.oneblog.api.interfaces.VoteType;
import net.oneblog.article.entity.VoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface VoteRepository extends JpaRepository<VoteEntity, Long> {

    @Query("select v from VoteEntity v where v.article.articleId = :articleId")
    Set<VoteEntity> findByArticleId(Long articleId);

    @Query("select v from VoteEntity v where v.user.userId = :userId")
    Set<VoteEntity> findByUserId(Long userId);

    @Query("select count(v) > 0 from VoteEntity v where v.article.articleId = :articleId and v.user.userId = :userId")
    boolean existsByArticleIdAndUserId(Long articleId, Long userId);

    @Query("select count(v) from VoteEntity v where v.article.articleId = :articleId")
    long countByArticleId(Long articleId);

    @Query("select count(v) from VoteEntity v where v.user.userId = :userId")
    long coundByUserId(Long userId);

    @Query(
        "select count(v) from VoteEntity v where v.article.articleId = :articleId and v.voteType = :type")
    long countByArticleIdAndType(@Param("articleId") Long articleId, @Param("type") VoteType type);

    @Modifying
    @Query("delete from VoteEntity v where v.user.userId = :userId")
    void deleteByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("delete from VoteEntity v where v.article.articleId = :articleId")
    void deleteByArticleId(@Param("articleId") Long articleId);
}
