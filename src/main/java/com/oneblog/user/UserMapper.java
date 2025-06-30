package com.oneblog.user;

import com.oneblog.user.dto.UserCreateDto;
import com.oneblog.user.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

/**
 * The type User mapper.
 */
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
	componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public abstract class UserMapper {

	/**
	 * Map user.
	 *
	 * @param userCreateDto the user create dto
	 * @return the user
	 */
	public abstract User map(UserCreateDto userCreateDto);

	/**
	 * Map user.
	 *
	 * @param userDto the user dto
	 * @return the user
	 */
	public abstract User map(UserDto userDto);

	/**
	 * Map user dto.
	 *
	 * @param user the user
	 * @return the user dto
	 */
	public abstract UserDto map(User user);
}
