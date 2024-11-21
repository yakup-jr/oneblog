package com.oneblog.user;

import com.oneblog.exceptions.NotFoundException;

public class UserNotFoundException extends NotFoundException {
	public UserNotFoundException(String message) {
		super(message);
	}
}
