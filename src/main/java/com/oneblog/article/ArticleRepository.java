package com.oneblog.article;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

	@Query(value = "select ar from Article ar where ar.user.userId = ?1")
	List<Article> findByUserId(Long userId);

	@Query("select count(ar)>0 from Article ar where ar.user.userId = ?1")
	boolean existsByUserId(Long userId);

	@Modifying
	@Query("delete from Article ar where ar.user.userId = ?1")
	void deleteByUserId(Long userId);

}
