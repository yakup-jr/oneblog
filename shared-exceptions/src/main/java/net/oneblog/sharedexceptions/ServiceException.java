package net.oneblog.sharedexceptions;

/**
 * The type Service exception.
 */
public class ServiceException extends RuntimeException {
	/**
	 * Instantiates a new Service exception.
	 *
	 * @param message the message
	 */
	public ServiceException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new Service exception.
	 *
	 * @param message the message
	 * @param cause   the cause
	 */
	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
