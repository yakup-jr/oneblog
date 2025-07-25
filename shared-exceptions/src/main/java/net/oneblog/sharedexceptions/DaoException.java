package net.oneblog.sharedexceptions;

/**
 * The type Dao exception.
 */
public class DaoException extends RuntimeException {
	/**
	 * Instantiates a new Dao exception.
	 *
	 * @param message the message
	 */
	public DaoException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new Dao exception.
	 *
	 * @param message the message
	 * @param cause   the cause
	 */
	public DaoException(String message, Throwable cause) {
		super(message, cause);
	}
}
