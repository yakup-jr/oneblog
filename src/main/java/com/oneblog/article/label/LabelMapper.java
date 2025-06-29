package com.oneblog.article.label;

import com.oneblog.article.label.dto.LabelCreateDto;
import com.oneblog.article.label.dto.LabelDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

/**
 * The type Label mapper.
 */
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
	componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public abstract class LabelMapper {

	/**
	 * Map label.
	 *
	 * @param dto the dto
	 * @return the label
	 */
	public abstract Label map(LabelCreateDto dto);

	/**
	 * Map label dto.
	 *
	 * @param model the model
	 * @return the label dto
	 */
	public abstract LabelDto map(Label model);

}
