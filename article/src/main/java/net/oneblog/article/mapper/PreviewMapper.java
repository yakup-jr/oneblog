package net.oneblog.article.mapper;

import net.oneblog.article.dto.PreviewCreateDto;
import net.oneblog.article.dto.PreviewDto;
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
     * @param previewCreateDto the preview create dto
     * @return the preview
     */
    PreviewEntity map(PreviewCreateDto previewCreateDto);

    /**
     * Map preview dto.
     *
     * @param previewEntity the preview
     * @return the preview dto
     */
    PreviewDto map(PreviewEntity previewEntity);

}
