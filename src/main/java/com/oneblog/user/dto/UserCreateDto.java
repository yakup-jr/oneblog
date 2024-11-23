package com.oneblog.user.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.server.core.Relation;

@Setter
@Getter
@Relation(collectionRelation = "users")
public class UserCreateDto {

	private String name;

	private String nickname;

	private String email;

	private String password;

}

