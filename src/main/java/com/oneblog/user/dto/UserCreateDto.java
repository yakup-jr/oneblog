package com.oneblog.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.server.core.Relation;

@Setter
@Getter
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

