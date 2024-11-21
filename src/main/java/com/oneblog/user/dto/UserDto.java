package com.oneblog.user.dto;

import com.oneblog.user.role.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserDto {

	private Long userId;

	private String name;

	private String nickname;

	private String email;

	private List<Role> roles;
}
