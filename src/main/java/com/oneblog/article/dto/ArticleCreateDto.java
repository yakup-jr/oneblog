package com.oneblog.article.dto;

import com.oneblog.article.label.Label;
import com.oneblog.article.preview.ArticlePreview;
import com.oneblog.user.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Relation(collectionRelation = "articles")
public class ArticleCreateDto {

	private String title;

	private String body;

	private LocalDateTime createTime;

	private ArticlePreview articlePreview;

	private List<Label> labels;

	private User user;

}
