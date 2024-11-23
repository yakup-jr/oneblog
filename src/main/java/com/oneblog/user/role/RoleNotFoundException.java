package com.oneblog.user.role;

import com.oneblog.exceptions.NotFoundException;

public class RoleNotFoundException extends NotFoundException {
	public RoleNotFoundException(String message) {
		super(message);
	}
}
