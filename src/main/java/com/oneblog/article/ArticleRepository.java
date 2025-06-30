package com.oneblog.article;

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
public interface ArticleRepository extends JpaRepository<Article, Long>, PagingAndSortingRepository<Article, Long> {

	/**
	 * Find by user id list.
	 *
	 * @param userId the user id
	 * @return the list
	 */
	@Query(value = "select ar from Article ar where ar.user.userId = ?1")
	List<Article> findByUserId(Long userId);

	/**
	 * Exists by user id boolean.
	 *
	 * @param userId the user id
	 * @return the boolean
	 */
	@Query("select count(ar)>0 from Article ar where ar.user.userId = ?1")
	boolean existsByUserId(Long userId);

	/**
	 * Delete by user id.
	 *
	 * @param userId the user id
	 */
	@Modifying
	@Query("delete from Article ar where ar.user.userId = ?1")
	void deleteByUserId(Long userId);

}
