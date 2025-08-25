package net.oneblog.sharedexceptions;

/**
 * The type Conflict exception.
 */
public class ConflictException extends RuntimeException {

    /**
     * Instantiates a new Conflict exception.
     *
     * @param message the message
     */
    public ConflictException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Conflict exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public ConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
