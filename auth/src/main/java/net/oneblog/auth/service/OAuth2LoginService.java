package net.oneblog.auth.service;

import net.oneblog.auth.dto.AuthenticationResponseDto;

/**
 * The interface O auth 2 login service.
 */
public interface OAuth2LoginService {

    /**
     * Gets authorization url.
     *
     * @return the authorization url
     */
    String getAuthorizationUrl();

    /**
     * Login authentication response dto.
     *
     * @param code the code
     * @return the authentication response dto
     */
    AuthenticationResponseDto login(String code);

}
