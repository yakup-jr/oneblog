package com.oneblog.article.label;

import com.oneblog.article.Article;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "T_LABEL")
@Entity
public class Label {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "LABEL_ID", nullable = false, updatable = false, unique = true)
	private Long labelId;

	@Column(name = "NAME", nullable = false, unique = true)
	@Enumerated(EnumType.STRING)
	private LabelName name;

	@ManyToMany(mappedBy = "labels", fetch = FetchType.LAZY)
	private List<Article> articles;
}
