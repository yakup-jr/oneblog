package net.oneblog.article.mapper;

import net.oneblog.article.models.PreviewCreateModel;
import net.oneblog.article.models.PreviewModel;
import net.oneblog.article.entity.PreviewEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

/**
 * The type Preview mapper.
 */
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PreviewMapper {

    /**
     * Map preview.
     *
     * @param previewCreateModel the preview create dto
     * @return the preview
     */
    PreviewEntity map(PreviewCreateModel previewCreateModel);

    /**
     * Map preview dto.
     *
     * @param previewEntity the preview
     * @return the preview dto
     */
    PreviewModel map(PreviewEntity previewEntity);

}
