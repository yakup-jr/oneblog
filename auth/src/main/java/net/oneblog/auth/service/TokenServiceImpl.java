package net.oneblog.auth.service;

import io.jsonwebtoken.io.SerialException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import net.oneblog.auth.entity.AuthEntity;
import net.oneblog.auth.entity.TokenEntity;
import net.oneblog.auth.models.AuthenticationResponseModel;
import net.oneblog.auth.models.RefreshTokenRequestModel;
import net.oneblog.auth.repository.AuthRepository;
import net.oneblog.auth.repository.TokenRepository;
import net.oneblog.sharedexceptions.ServiceException;
import net.oneblog.user.repository.UserRepository;
import net.oneblog.user.service.UserService;
import net.oneblog.validationapi.models.ValidatedUserModel;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Token service.
 */
@Service
@AllArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserService userService;
    private final TokenRepository tokenRepository;
    private final AuthRepository authRepository;

    @Override
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

            revokeAllTokensForUser(user);
            saveUserToken(accessToken, refreshToken, user);

            return new AuthenticationResponseModel(accessToken, refreshToken);
        }
        throw new ServiceException("Invalid token");
    }

    @Override
    public AuthenticationResponseModel reIssueRefreshToken(RefreshTokenRequestModel refreshToken) {
        String username = jwtService.extractUsername(refreshToken.refreshToken());

        ValidatedUserModel userEntity = userService.findByNickname(username);

        if (jwtService.isValidRefresh(refreshToken.refreshToken(), userEntity)) {

            String newAccessToken = jwtService.generateAccessToken(userEntity);
            String newRefreshToken = jwtService.generateRefreshToken(userEntity);

            revokeAllTokensForUser(userEntity);
            saveUserToken(newAccessToken, newRefreshToken, userEntity);

            return new AuthenticationResponseModel(newAccessToken, newRefreshToken);
        }
        throw new ServiceException("Invalid token");
    }

    @Override
    public void revokeAllTokensForUser(ValidatedUserModel user) {
        List<TokenEntity> validToken = tokenRepository.findAllAccessTokenByUser(user.userId());

        if (!validToken.isEmpty()) {
            validToken.forEach(token -> {
                token.setIsRevoke(true);
            });
        }

        tokenRepository.saveAll(validToken);
    }

    @Override
    public void saveUserToken(String accessToken, String refreshToken, ValidatedUserModel user) {
        AuthEntity authEntity =
            authRepository.findByEmail(user.email()).orElseThrow(() -> new SerialException(
                "user not found"));

        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setAccessToken(accessToken);
        tokenEntity.setRefreshToken(refreshToken);
        authEntity.getTokens().add(tokenEntity);

        tokenRepository.save(tokenEntity);
    }
}
