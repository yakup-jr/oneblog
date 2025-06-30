package com.oneblog.user;

import com.oneblog.exceptions.NotFoundException;

/**
 * The type User not found exception.
 */
public class UserNotFoundException extends NotFoundException {
	/**
	 * Instantiates a new User not found exception.
	 *
	 * @param message the message
	 */
	public UserNotFoundException(String message) {
		super(message);
	}
}
