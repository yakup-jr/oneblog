package com.oneblog.article.label.dto;

import com.oneblog.article.label.LabelName;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.server.core.Relation;

@Setter
@Getter
@Relation(collectionRelation = "labels")
public class LabelCreateDto {

	@NotNull
	private LabelName name;

}
