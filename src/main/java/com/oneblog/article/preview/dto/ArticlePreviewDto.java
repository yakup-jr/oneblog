package com.oneblog.article.preview.dto;


import com.oneblog.article.dto.ArticleDto;
import com.oneblog.article.paragraph.dto.ParagraphDto;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ArticlePreviewDto {

	private Long articlePreviewId;

	private ParagraphDto paragraph;

	private ArticleDto article;
}
