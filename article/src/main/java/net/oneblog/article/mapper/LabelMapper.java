package net.oneblog.article.mapper;

import net.oneblog.article.dto.LabelCreateDto;
import net.oneblog.article.dto.LabelDto;
import net.oneblog.article.entity.LabelEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

/**
 * The type Label mapper.
 */
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.WARN)
public interface LabelMapper {

    /**
     * Map label.
     *
     * @param dto the dto
     * @return the label
     */
    LabelEntity map(LabelCreateDto dto);

    /**
     * Map label dto.
     *
     * @param model the model
     * @return the label dto
     */
    LabelDto map(LabelEntity model);

}
