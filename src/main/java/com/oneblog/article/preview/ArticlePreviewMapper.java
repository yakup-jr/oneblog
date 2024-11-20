package com.oneblog.article.preview;

import com.oneblog.article.Article;
import com.oneblog.article.preview.dto.ArticlePreviewCreateDto;
import com.oneblog.article.preview.dto.ArticlePreviewDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
	componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ArticlePreviewMapper {

	public abstract ArticlePreview map(ArticlePreviewCreateDto articlePreviewCreateDto);

	public abstract ArticlePreviewDto map(ArticlePreview articlePreview);

}
