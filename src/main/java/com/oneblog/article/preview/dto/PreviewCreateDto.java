package com.oneblog.article.preview.dto;

import com.oneblog.article.dto.ArticleDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.server.core.Relation;

@Setter
@Getter
@Relation(collectionRelation = "previews")
public class PreviewCreateDto {

	private String body;

	private ArticleDto article;

}
