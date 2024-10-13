package com.oneblog.article.preview;

import com.oneblog.article.Article;
import com.oneblog.article.paragraph.Paragraph;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "T_ARTICLE_PREVIEW")
@Entity
public class ArticlePreview {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ARTICLE_PREVIEW_ID", nullable = false, updatable = false, unique = true)
	private Long articlePreviewId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARAGRAPH_ID")
	private Paragraph paragraph;

	@OneToOne(mappedBy = "articlePreview", cascade = CascadeType.ALL)
	@JoinColumn(name = "ARTICLE_ID")
	private Article article;
}
