package net.oneblog.email.exceptions;

/**
 * The type Email verification code already send exception.
 */
public class EmailVerificationCodeAlreadySendException extends RuntimeException {
    /**
     * Instantiates a new Email verification code already send exception.
     *
     * @param message the message
     */
    public EmailVerificationCodeAlreadySendException(String message) {
        super(message);
    }

}
