package net.oneblog.validationapi.mappers;

import net.oneblog.api.dto.UserDto;
import net.oneblog.validationapi.models.ValidatedUserModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ValidatedUserModelMapper {

    UserDto map(ValidatedUserModel validatedUserModel);

    ValidatedUserModel map(UserDto userDto);

}
