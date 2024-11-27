package com.oneblog.article.preview.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.server.core.Relation;

@Setter
@Getter
@Relation(collectionRelation = "previews")
public class PreviewCreateDto {

	@NotNull
	@Length(min = 10, max = 1000)
	private String body;

}
