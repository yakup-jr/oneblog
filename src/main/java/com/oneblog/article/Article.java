package com.oneblog.article;

import com.oneblog.article.label.Label;
import com.oneblog.article.paragraph.Paragraph;
import com.oneblog.article.preview.ArticlePreview;
import com.oneblog.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "T_ARTICLE")
@Entity
public class Article {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "ARTICLE_ID", nullable = false, updatable = false, unique = true)
	private Long articleId;

	@Column(name = "TITLE", nullable = false)
	private String title;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "t_article_paragraph",
		joinColumns = @JoinColumn(name = "article_id", foreignKey = @ForeignKey(name = "fk_article_paragraph")),
		inverseJoinColumns = @JoinColumn(name = "paragraph_id",
			foreignKey = @ForeignKey(name = "fk_paragraph_article")))
	private List<Paragraph> paragraphs;


	@Column(name = "CREATED_AT", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ARTICLE_PREVIEW_ID", foreignKey = @ForeignKey(name = "fk_article_article_preview"))
	private ArticlePreview articlePreview;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "T_ARTICLE_LABEL",
		joinColumns = @JoinColumn(name = "ARTICLE_ID", foreignKey = @ForeignKey(name = "fk_article_label"),
			referencedColumnName = "article_id"),
		inverseJoinColumns = @JoinColumn(name = "LABEL_ID", foreignKey = @ForeignKey(name = "fk_label_article"),
			referencedColumnName = "label_id"))
	private List<Label> labels;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", foreignKey = @ForeignKey(name = "fk_article_user"))
	private User user;

}
