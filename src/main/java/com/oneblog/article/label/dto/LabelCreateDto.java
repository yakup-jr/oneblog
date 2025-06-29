package com.oneblog.article.label.dto;

import com.oneblog.article.label.LabelName;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.server.core.Relation;

/**
 * The type Label create dto.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Relation(collectionRelation = "labels")
public class LabelCreateDto {

	@NotNull
	private LabelName name;

}
