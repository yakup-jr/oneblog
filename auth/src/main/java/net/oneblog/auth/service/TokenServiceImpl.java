package net.oneblog.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import net.oneblog.auth.models.AuthenticationResponseModel;
import net.oneblog.auth.models.RefreshTokenRequestModel;
import net.oneblog.sharedexceptions.ServiceException;
import net.oneblog.user.service.UserService;
import net.oneblog.validationapi.models.ValidatedUserModel;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type Token service.
 */
@Service
@AllArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final JwtService jwtService;
    private final UserService userService;
    private final TokenManagerService tokenManagerService;

    @Override
    @Transactional
    public AuthenticationResponseModel reIssueAccessToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new ServiceException("Header is missing or invalid");
        }

        String token = authorizationHeader.substring(7);
        String username = jwtService.extractUsername(token);

        ValidatedUserModel user = userService.findByNickname(username);

        if (jwtService.isValidRefresh(token, user)) {
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            tokenManagerService.revokeAllTokensForUser(user);
            tokenManagerService.saveUserToken(accessToken, refreshToken, user);

            return new AuthenticationResponseModel(accessToken, refreshToken);
        }
        throw new ServiceException("Invalid token");
    }

    @Override
    @Transactional
    public AuthenticationResponseModel reIssueRefreshToken(RefreshTokenRequestModel refreshToken) {
        String username = jwtService.extractUsername(refreshToken.refreshToken());

        ValidatedUserModel userEntity = userService.findByNickname(username);

        if (jwtService.isValidRefresh(refreshToken.refreshToken(), userEntity)) {

            String newAccessToken = jwtService.generateAccessToken(userEntity);
            String newRefreshToken = jwtService.generateRefreshToken(userEntity);

            tokenManagerService.revokeAllTokensForUser(userEntity);
            tokenManagerService.saveUserToken(newAccessToken, newRefreshToken, userEntity);

            return new AuthenticationResponseModel(newAccessToken, newRefreshToken);
        }
        throw new ServiceException("Invalid token");
    }
}
