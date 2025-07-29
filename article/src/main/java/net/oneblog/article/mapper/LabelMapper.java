package net.oneblog.article.mapper;

import net.oneblog.article.models.LabelCreateModel;
import net.oneblog.article.models.LabelModel;
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
    LabelEntity map(LabelCreateModel dto);

    /**
     * Map label dto.
     *
     * @param model the model
     * @return the label dto
     */
    LabelModel map(LabelEntity model);

}
