package com.oneblog.article.preview;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticlePreviewRepository extends JpaRepository<ArticlePreview, Long> {
}
