package net.oneblog.article.mapper;

import net.oneblog.article.models.ArticleCreateModel;
import net.oneblog.article.models.ArticleModel;
import net.oneblog.article.entity.ArticleEntity;
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
     * @param articleCreateModel the article create dto
     * @return the article
     */
    @Mapping(source = "labels", target = "labelEntities")
    @Mapping(source = "preview", target = "previewEntity")
    @Mapping(source = "user", target = "userEntity")
    ArticleEntity map(ArticleCreateModel articleCreateModel);

    /**
     * Map article.
     *
     * @param articleModel the article dto
     * @return the article
     */
    ArticleEntity map(ArticleModel articleModel);

    /**
     * Map article dto.
     *
     * @param articleEntity the article
     * @return the article dto
     */
    @Mapping(source = "labelEntities", target = "labels")
    @Mapping(source = "previewEntity", target = "preview")
    @Mapping(source = "userEntity", target = "user")
    ArticleModel map(ArticleEntity articleEntity);
}
