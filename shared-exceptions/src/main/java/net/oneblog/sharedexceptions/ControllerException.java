package net.oneblog.sharedexceptions;

/**
 * The type Controller exception.
 */
public class ControllerException extends RuntimeException {
    /**
     * Instantiates a new Controller exception.
     *
     * @param message the message
     */
    public ControllerException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Controller exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public ControllerException(String message, Throwable cause) {
        super(message, cause);
    }
}
