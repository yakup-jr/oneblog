package net.oneblog.user.exceptions;

import net.oneblog.sharedexceptions.NotFoundException;

/**
 * The type User didn't find an exception.
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
