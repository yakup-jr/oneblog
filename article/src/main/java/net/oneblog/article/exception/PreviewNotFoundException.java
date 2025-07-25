package net.oneblog.article.exception;

import net.oneblog.sharedexceptions.NotFoundException;

/**
 * The type Preview not found exception.
 */
public class PreviewNotFoundException extends NotFoundException {
	/**
	 * Instantiates a new Preview not found exception.
	 *
	 * @param message the message
	 */
	public PreviewNotFoundException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new Preview not found exception.
	 *
	 * @param message the message
	 * @param cause   the cause
	 */
	public PreviewNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
