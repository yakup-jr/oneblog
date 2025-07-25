package net.oneblog.article.exception;


import net.oneblog.sharedexceptions.NotFoundException;

/**
 * The type Label not found exception.
 */
public class LabelNotFoundException extends NotFoundException {

	/**
	 * Instantiates a new Label not found exception.
	 *
	 * @param message the message
	 */
	public LabelNotFoundException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new Label not found exception.
	 *
	 * @param message the message
	 * @param cause   the cause
	 */
	public LabelNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
