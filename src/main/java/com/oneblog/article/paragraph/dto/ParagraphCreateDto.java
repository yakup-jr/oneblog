package com.oneblog.article.paragraph.dto;

import com.oneblog.article.Article;
import com.oneblog.article.preview.ArticlePreview;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ParagraphCreateDto {

	private String text;

	private Article article;

	private ArticlePreview articlePreview;

}
