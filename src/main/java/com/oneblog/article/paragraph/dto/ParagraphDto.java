package com.oneblog.article.paragraph.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.server.core.Relation;

@Setter
@Getter
@Relation(collectionRelation = "paragraphs")
public class ParagraphDto {

	private Long paragraphId;

	private String text;

}
