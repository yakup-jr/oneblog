package net.oneblog.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import net.oneblog.auth.models.AuthenticationResponseModel;
import net.oneblog.auth.models.RefreshTokenRequestModel;
import net.oneblog.user.entity.UserEntity;
import net.oneblog.validationapi.models.ValidatedUserModel;

/**
 * The interface Token service.
 */
public interface TokenService {

    /**
     * Re issue refresh token authentication response dto.
     *
     * @param refreshToken the refresh token
     * @return the authentication response dto
     */
    AuthenticationResponseModel reIssueRefreshToken(RefreshTokenRequestModel refreshToken);

    /**
     * Re issue access token authentication response dto.
     *
     * @param request the request
     * @return the authentication response dto
     */
    AuthenticationResponseModel reIssueAccessToken(HttpServletRequest request);

    /**
     * Revoke all tokens for user.
     *
     * @param user the user
     */
    void revokeAllTokensForUser(ValidatedUserModel user);

    /**
     * Save user token.
     *
     * @param accessToken  the access token
     * @param refreshToken the refresh token
     * @param user         the user
     */
    void saveUserToken(String accessToken, String refreshToken, ValidatedUserModel user);


}
