package net.oneblog.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import net.oneblog.user.controller.UserController;
import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.server.core.Relation;

/**
 * The type User dto.
 */
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

}
