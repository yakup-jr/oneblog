package com.oneblog.user.role;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.hateoas.server.core.Relation;

/**
 * The type Role dto.
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Relation(collectionRelation = "roles")
public class RoleDto {

	@NotNull
	private RoleName name;

}
