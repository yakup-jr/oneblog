package net.oneblog.user.mappers;

import net.oneblog.api.dto.UserDto;
import net.oneblog.user.entity.UserEntity;
import net.oneblog.user.models.UserCreateRequest;
import net.oneblog.validationapi.models.ValidatedUserModel;
import org.mapstruct.*;

/**
 * The type User mapper.
 */
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.WARN)
public interface UserMapper {
    /**
     * Map user.
     *
     * @param userCreateRequest the user create dto
     * @return the user
     */
    @Mapping(target = "userId", ignore = true)
    UserEntity map(UserCreateRequest userCreateRequest);

    /**
     * Map user.
     *
     * @param userDto the user dto
     * @return the user
     */
    UserEntity map(ValidatedUserModel userDto);

    /**
     * Map user dto.
     *
     * @param userEntity the user entity
     * @return the user dto
     */
    ValidatedUserModel map(UserEntity userEntity);

    ValidatedUserModel map(UserDto userDto);
}
