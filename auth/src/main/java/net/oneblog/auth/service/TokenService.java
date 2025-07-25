package net.oneblog.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import net.oneblog.auth.dto.AuthenticationResponseDto;
import net.oneblog.auth.dto.RefreshTokenRequestDto;
import net.oneblog.user.entity.UserEntity;

public interface TokenService {

    AuthenticationResponseDto reIssueRefreshToken(RefreshTokenRequestDto refreshToken);

    AuthenticationResponseDto reIssueAccessToken(HttpServletRequest request);

    void revokeAllTokensForUser(UserEntity user);

    void saveUserToken(String accessToken, String refreshToken, UserEntity user);


}
