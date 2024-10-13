package com.oneblog.article.dto;


import com.oneblog.article.label.dto.LabelDto;
import com.oneblog.article.paragraph.dto.ParagraphDto;
import com.oneblog.article.preview.ArticlePreview;
import com.oneblog.user.dto.UserDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class ArticleDto {

	private Long articleId;

	private String title;

	private List<ParagraphDto> paragraphs;

	private LocalDateTime createdAt;

	private ArticlePreview articlePreview;

	private List<LabelDto> labels;

	private UserDto user;


}
