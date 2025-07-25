package net.oneblog.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import net.oneblog.auth.dto.AuthenticationResponseDto;
import net.oneblog.auth.dto.RefreshTokenRequestDto;
import net.oneblog.user.entity.UserEntity;

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
    AuthenticationResponseDto reIssueRefreshToken(RefreshTokenRequestDto refreshToken);

    /**
     * Re issue access token authentication response dto.
     *
     * @param request the request
     * @return the authentication response dto
     */
    AuthenticationResponseDto reIssueAccessToken(HttpServletRequest request);

    /**
     * Revoke all tokens for user.
     *
     * @param user the user
     */
    void revokeAllTokensForUser(UserEntity user);

    /**
     * Save user token.
     *
     * @param accessToken  the access token
     * @param refreshToken the refresh token
     * @param user         the user
     */
    void saveUserToken(String accessToken, String refreshToken, UserEntity user);


}
