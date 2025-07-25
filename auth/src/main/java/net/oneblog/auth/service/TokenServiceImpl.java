package net.oneblog.auth.service;

import io.jsonwebtoken.io.SerialException;
import jakarta.servlet.http.HttpServletRequest;
import net.oneblog.auth.dto.AuthenticationResponseDto;
import net.oneblog.auth.dto.RefreshTokenRequestDto;
import net.oneblog.auth.entity.AuthEntity;
import net.oneblog.auth.entity.TokenEntity;
import net.oneblog.auth.repository.AuthRepository;
import net.oneblog.auth.repository.TokenRepository;
import net.oneblog.sharedexceptions.ServiceException;
import net.oneblog.user.entity.UserEntity;
import net.oneblog.user.exceptions.UserNotFoundException;
import net.oneblog.user.repository.UserRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TokenServiceImpl implements TokenService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final AuthRepository authRepository;

    public TokenServiceImpl(JwtService jwtService, UserRepository userRepository,
                            TokenRepository tokenRepository, AuthRepository authRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.authRepository = authRepository;
    }

    @Override
    public AuthenticationResponseDto reIssueAccessToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new ServiceException("Header is missing or invalid");
        }

        String token = authorizationHeader.substring(7);
        String username = jwtService.extractUsername(token);

        UserEntity user = userRepository.findByNickname(username)
            .orElseThrow(() -> new UserNotFoundException("User" +
                " not found"));

        if (jwtService.isValidRefresh(token, user)) {
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            revokeAllTokensForUser(user);
            saveUserToken(accessToken, refreshToken, user);

            return new AuthenticationResponseDto(accessToken, refreshToken);
        }
        throw new ServiceException("Invalid token");
    }

    @Override
    public AuthenticationResponseDto reIssueRefreshToken(RefreshTokenRequestDto refreshToken) {
        String username = jwtService.extractUsername(refreshToken.refreshToken());

        UserEntity userEntity = userRepository.findByNickname(username).orElseThrow(
            () -> new UserNotFoundException("User with username " + username + " not found"));

        if (jwtService.isValidRefresh(refreshToken.refreshToken(), userEntity)) {

            String newAccessToken = jwtService.generateAccessToken(userEntity);
            String newRefreshToken = jwtService.generateRefreshToken(userEntity);

            revokeAllTokensForUser(userEntity);
            saveUserToken(newAccessToken, newRefreshToken, userEntity);

            return new AuthenticationResponseDto(newAccessToken, newRefreshToken);
        }
        throw new ServiceException("Invalid token");
    }

    @Override
    public void revokeAllTokensForUser(UserEntity user) {
        List<TokenEntity> validToken = tokenRepository.findAllAccessTokenByUser(user.getUserId());

        if (!validToken.isEmpty()) {
            validToken.forEach(token -> {
                token.setIsRevoke(true);
            });
        }

        tokenRepository.saveAll(validToken);
    }

    @Override
    public void saveUserToken(String accessToken, String refreshToken, UserEntity user) {
        AuthEntity authEntity =
            authRepository.findByEmail(user.getEmail()).orElseThrow(() -> new SerialException(
                "user not found"));

        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setAccessToken(accessToken);
        tokenEntity.setRefreshToken(refreshToken);
        authEntity.getTokens().add(tokenEntity);

        tokenRepository.save(tokenEntity);
    }
}
