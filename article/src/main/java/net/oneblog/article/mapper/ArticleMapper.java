package net.oneblog.article.mapper;

import net.oneblog.article.dto.ArticleCreateDto;
import net.oneblog.article.dto.ArticleDto;
import net.oneblog.article.entity.ArticleEntity;
import net.oneblog.user.mappers.UserMapper;
import org.mapstruct.*;

/**
 * The type Article mapper.
 */
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ArticleMapper {

    /**
     * Map article.
     *
     * @param articleCreateDto the article create dto
     * @return the article
     */
    @Mapping(source = "labels", target = "labelEntities")
    @Mapping(source = "preview", target = "previewEntity")
    @Mapping(source = "user", target = "userEntity")
    ArticleEntity map(ArticleCreateDto articleCreateDto);

    /**
     * Map article.
     *
     * @param articleDto the article dto
     * @return the article
     */
    ArticleEntity map(ArticleDto articleDto);

    /**
     * Map article dto.
     *
     * @param articleEntity the article
     * @return the article dto
     */
    @Mapping(source = "labelEntities", target = "labels")
    @Mapping(source = "previewEntity", target = "preview")
    @Mapping(source = "userEntity", target = "user")
    ArticleDto map(ArticleEntity articleEntity);
}
