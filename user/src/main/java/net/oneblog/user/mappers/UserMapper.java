package net.oneblog.user.mappers;

import net.oneblog.user.dto.UserCreateDto;
import net.oneblog.user.dto.UserDto;
import net.oneblog.user.entity.UserEntity;
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
     * @param userCreateDto the user create dto
     * @return the user
     */
    @Mapping(target = "userId", ignore = true)
    UserEntity map(UserCreateDto userCreateDto);

    /**
     * Map user.
     *
     * @param userDto the user dto
     * @return the user
     */
    UserEntity map(UserDto userDto);

    /**
     * Map user dto.
     *
     * @param userEntity the user entity
     * @return the user dto
     */
    UserDto map(UserEntity userEntity);
}
