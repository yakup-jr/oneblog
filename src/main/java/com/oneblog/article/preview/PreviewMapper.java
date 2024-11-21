package com.oneblog.article.preview;

import com.oneblog.article.preview.dto.PreviewCreateDto;
import com.oneblog.article.preview.dto.PreviewDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
	componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class PreviewMapper {

	public abstract Preview map(PreviewCreateDto previewCreateDto);

	public abstract PreviewDto map(Preview preview);

}
