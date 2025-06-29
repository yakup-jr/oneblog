package com.oneblog.article.preview;

import com.oneblog.article.preview.dto.PreviewCreateDto;
import com.oneblog.article.preview.dto.PreviewDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

/**
 * The type Preview mapper.
 */
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
	componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class PreviewMapper {

	/**
	 * Map preview.
	 *
	 * @param previewCreateDto the preview create dto
	 * @return the preview
	 */
	public abstract Preview map(PreviewCreateDto previewCreateDto);

	/**
	 * Map preview dto.
	 *
	 * @param preview the preview
	 * @return the preview dto
	 */
	public abstract PreviewDto map(Preview preview);

}
