package com.oneblog.article.dto;


import com.oneblog.article.label.dto.LabelDto;
import com.oneblog.article.preview.dto.PreviewDto;
import com.oneblog.user.dto.UserDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Relation(collectionRelation = "articles")
public class ArticleDto {

	@NotNull
	@Min(value = 1L)
	private Long articleId;

	@NotNull
	@Length(min = 1, max = 255)
	private String title;

	@NotNull
	@Length(min = 10, max = 65000)
	private String body;

	@NotNull
	private LocalDateTime createdAt;

	@NotNull
	private PreviewDto preview;

	@NotNull
	private List<LabelDto> labels;

	@NotNull
	private UserDto user;
}
