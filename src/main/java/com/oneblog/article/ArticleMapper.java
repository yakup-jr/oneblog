package com.oneblog.article;

import com.oneblog.article.dto.ArticleCreateDto;
import com.oneblog.article.dto.ArticleDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
	componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public abstract class ArticleMapper {

	public abstract Article map(ArticleCreateDto articleCreateDto);

	public abstract Article map(ArticleDto articleDto);

	public abstract ArticleDto map(Article article);
}
