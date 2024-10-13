package com.oneblog.article.paragraph;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParagraphRepository extends JpaRepository<Paragraph, Long> {

	@Query("select p from Paragraph p where p.articlePreview.articlePreviewId = ?1")
	Optional<List<Paragraph>> findByArticlePreviewId(Long previewId);

	@Query("select p from Paragraph p where p.article.articleId = ?1")
	List<Paragraph> findByArticleId(Long articleId);

}
