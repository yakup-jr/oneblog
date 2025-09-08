package net.oneblog.api.interfaces;

/**
 * The interface User domain.
 */
public interface UserDomain {
    /**
     * Gets user id.
     *
     * @return the user id
     */
    Long getUserId();

    /**
     * Gets name.
     *
     * @return the name
     */
    String getName();

    /**
     * Gets nickname.
     *
     * @return the nickname
     */
    String getNickname();

    /**
     * Gets email.
     *
     * @return the email
     */
    String getEmail();

}
