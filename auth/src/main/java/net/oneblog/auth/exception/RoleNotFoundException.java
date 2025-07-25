package net.oneblog.auth.exception;

import net.oneblog.sharedexceptions.NotFoundException;

/**
 * The type Role not found exception.
 */
public class RoleNotFoundException extends NotFoundException {
	/**
	 * Instantiates a new Role not found exception.
	 *
	 * @param message the message
	 */
	public RoleNotFoundException(String message) {
		super(message);
	}
}
