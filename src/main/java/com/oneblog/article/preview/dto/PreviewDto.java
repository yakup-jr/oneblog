package com.oneblog.article.preview.dto;


import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.server.core.Relation;

@Setter
@Getter
@Relation(collectionRelation = "previews")
public class PreviewDto {

	private Long articlePreviewId;

	private String body;

}
