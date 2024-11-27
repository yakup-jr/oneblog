package com.oneblog.user.role;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.server.core.Relation;

@Setter
@Getter
@Relation(collectionRelation = "roles")
public class RoleDto {

	@NotNull
	private RoleName name;

}
