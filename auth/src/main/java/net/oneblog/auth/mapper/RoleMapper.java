package net.oneblog.auth.mapper;

import net.oneblog.auth.dto.RoleDto;
import net.oneblog.auth.entity.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

/**
 * The type Role mapper.
 */
@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
	componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface RoleMapper {

	/**
	 * Map role.
	 *
	 * @param role the role
	 * @return the role
	 */
	RoleEntity map(RoleDto role);

	/**
	 * Map role dto.
	 *
	 * @param roleEntity the role
	 * @return the role dto
	 */
	RoleDto map(RoleEntity roleEntity);

}
