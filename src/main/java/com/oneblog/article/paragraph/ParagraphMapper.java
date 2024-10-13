package com.oneblog.article.paragraph;

import com.oneblog.article.paragraph.dto.ParagraphCreateDto;
import com.oneblog.article.paragraph.dto.ParagraphDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, componentModel =
	MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ParagraphMapper {

	public abstract Paragraph map(ParagraphCreateDto dto);

	public abstract ParagraphDto map(Paragraph model);

}
