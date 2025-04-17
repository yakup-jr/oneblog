package com.oneblog.user.dto;

import com.oneblog.user.UserController;
import com.oneblog.user.role.RoleDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.server.core.Relation;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Relation(collectionRelation = "users")
public class UserDto {

	@NotNull
	@Min(1L)
	private Long userId;

	@NotNull(groups = UserController.class)
	@Length(min = 2, max = 60, groups = UserController.class)
	private String name;

	@NotNull(groups = UserController.class)
	@Length(min = 2, max = 60, groups = UserController.class)
	private String nickname;

	@NotNull(groups = UserController.class)
	@Email(groups = UserController.class)
	private String email;

	@NotNull(groups = UserController.class)
	@Valid
	private List<RoleDto> roles;
}
