package com.oneblog.internal.roles;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class Role {

	private Long id;

	private RoleName name;

	public Role(RoleName name) {
		this.name = name;
	}
}
