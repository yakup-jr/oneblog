package com.oneblog.article.label;

import com.oneblog.article.label.dto.LabelCreateDto;
import com.oneblog.article.label.dto.LabelDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
	componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class LabelMapper {

	public abstract Label map(LabelCreateDto dto);

	public abstract LabelDto map(Label model);

}
