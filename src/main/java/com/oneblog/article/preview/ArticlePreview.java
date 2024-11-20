package com.oneblog.article.preview;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.oneblog.article.Article;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_article_preview")
@Entity
public class ArticlePreview {

	@Id
	@SequenceGenerator(name = "preview_seq", sequenceName = "preview_sequence", initialValue = 10, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "preview_seq")
	@Column(name = "article_preview_id", nullable = false, updatable = false, unique = true)
	private Long articlePreviewId;

	@Column(name = "body")
	private String body;

	@JsonBackReference
	@OneToOne(mappedBy = "articlePreview")
	private Article article;
}
