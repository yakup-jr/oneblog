package com.oneblog.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.server.core.Relation;

/**
 * The type User create dto.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Relation(collectionRelation = "users")
public class UserCreateDto {

	@NotNull
	@Length(min = 2, max = 60)
	private String name;

	@NotNull
	@Length(min = 2, max = 60)
	private String nickname;

	@NotNull
	@Email
	private String email;

	@NotNull
	@Length(min = 8, max = 60)
	private String password;

}

