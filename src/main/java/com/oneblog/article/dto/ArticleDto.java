package com.oneblog.article.dto;


import com.oneblog.article.label.dto.LabelDto;
import com.oneblog.article.preview.dto.ArticlePreviewDto;
import com.oneblog.user.dto.UserDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Relation(collectionRelation = "articles")
public class ArticleDto {

	private Long articleId;

	private String title;

	private String body;

	private LocalDateTime createdAt;

	private ArticlePreviewDto articlePreview;

	private List<LabelDto> labels;

	private UserDto user;
}
