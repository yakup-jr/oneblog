package com.oneblog.user;

import com.oneblog.user.dto.UserCreateDto;
import com.oneblog.user.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
	componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public abstract class UserMapper {

	public abstract User map(UserCreateDto userCreateDto);

	public abstract User map(UserDto userDto);

	public abstract UserDto map(User user);
}
