package net.oneblog.api.interfaces;

/**
 * The interface Token domain.
 */
public interface TokenDomain {
    /**
     * Gets token id.
     *
     * @return the token id
     */
    Long getTokenId();

    /**
     * Gets access token.
     *
     * @return the access token
     */
    String getAccessToken();

    /**
     * Gets refresh token.
     *
     * @return the refresh token
     */
    String getRefreshToken();

    /**
     * Is revoked boolean.
     *
     * @return the boolean
     */
    Boolean isRevoked();
}
