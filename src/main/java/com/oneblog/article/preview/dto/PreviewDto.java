package com.oneblog.article.preview.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.server.core.Relation;

/**
 * The type Preview dto.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Relation(collectionRelation = "previews")
public class PreviewDto {

	@NotNull
	@Min(1L)
	private Long articlePreviewId;

	@NotNull
	@Length(min = 10, max = 1000)
	private String body;

}
