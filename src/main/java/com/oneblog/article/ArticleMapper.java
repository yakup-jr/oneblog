package com.oneblog.article;

import com.oneblog.article.dto.ArticleCreateDto;
import com.oneblog.article.dto.ArticleDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

/**
 * The type Article mapper.
 */
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
	componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public abstract class ArticleMapper {

	/**
	 * Map article.
	 *
	 * @param articleCreateDto the article create dto
	 * @return the article
	 */
	public abstract Article map(ArticleCreateDto articleCreateDto);

	/**
	 * Map article.
	 *
	 * @param articleDto the article dto
	 * @return the article
	 */
	public abstract Article map(ArticleDto articleDto);

	/**
	 * Map article dto.
	 *
	 * @param article the article
	 * @return the article dto
	 */
	public abstract ArticleDto map(Article article);
}
