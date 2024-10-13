package com.oneblog.article.paragraph;

import com.oneblog.article.Article;
import com.oneblog.article.preview.ArticlePreview;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "T_PARAGRAPH")
@Entity
public class Paragraph {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "PARAGRAPH_ID", nullable = false, updatable = false, unique = true)
	private Long paragraphId;

	@Column(name = "TEXT", nullable = false)
	private String text;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "article_id")
	private Article article;

	@OneToOne(mappedBy = "paragraph")
	@JoinColumn(name = "article_preview_id")
	private ArticlePreview articlePreview;
}
