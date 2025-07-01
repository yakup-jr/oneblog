package com.oneblog.user.role;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

/**
 * The type Role mapper.
 */
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
	componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public abstract class RoleMapper {

	/**
	 * Map role.
	 *
	 * @param role the role
	 * @return the role
	 */
	public abstract Role map(RoleDto role);

	/**
	 * Map role dto.
	 *
	 * @param role the role
	 * @return the role dto
	 */
	public abstract RoleDto map(Role role);

}
