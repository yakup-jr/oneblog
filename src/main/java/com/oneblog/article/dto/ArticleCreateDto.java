package com.oneblog.article.dto;

import com.oneblog.article.label.dto.LabelDto;
import com.oneblog.article.preview.dto.PreviewCreateDto;
import com.oneblog.user.dto.UserDto;
import jakarta.validation.Valid;
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
public class ArticleCreateDto {

	@NotNull
	@Length(min = 1, max = 255)
	private String title;

	@NotNull
	@Length(min = 10, max = 65000)
	private String body;

	private LocalDateTime createTime;

	@NotNull
	@Valid
	private PreviewCreateDto preview;

	@NotNull
	@Valid
	private List<LabelDto> labels;

	@NotNull
	@Valid
	private UserDto user;

}
