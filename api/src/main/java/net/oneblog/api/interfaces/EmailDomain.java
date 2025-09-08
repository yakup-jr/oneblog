package net.oneblog.api.interfaces;

import java.time.LocalDateTime;

/**
 * The interface Email domain.
 */
public interface EmailDomain {
    /**
     * Gets email id.
     *
     * @return the email id
     */
    Long getEmailId();

    /**
     * Gets code.
     *
     * @return the code
     */
    String getCode();

    /**
     * Gets expired at.
     *
     * @return the expired at
     */
    LocalDateTime getExpiredAt();

    /**
     * Gets user.
     *
     * @return the user
     */
    String getEmail();
}
