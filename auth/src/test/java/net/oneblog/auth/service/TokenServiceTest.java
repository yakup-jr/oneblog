package net.oneblog.auth.service;

import io.jsonwebtoken.io.SerialException;
import jakarta.servlet.http.HttpServletRequest;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @InjectMocks
    private TokenServiceImpl tokenService;

    @Mock
    private JwtService jwtService;
    @Mock
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private AuthRepository authRepository;
    @Mock
    private HttpServletRequest request;

    @Test
    void reIssueAccessToken_Success() {
        String token = "valid-refresh-token";
        String username = "testuser";
        ValidatedUserModel userModel = ValidatedUserModel.builder()
            .userId(1L)
            .nickname(username)
            .email("test@example.com")
            .build();

        AuthEntity authEntity = AuthEntity.builder()
            .tokens(new ArrayList<>())
            .build();

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + token);
        when(jwtService.extractUsername(token)).thenReturn(username);
        when(userService.findByNickname(username)).thenReturn(userModel);
        when(jwtService.isValidRefresh(token, userModel)).thenReturn(true);
        when(jwtService.generateAccessToken(userModel)).thenReturn("new-access-token");
        when(jwtService.generateRefreshToken(userModel)).thenReturn("new-refresh-token");
        when(tokenRepository.findAllAccessTokenByUser(userModel.userId())).thenReturn(new ArrayList<>());
        when(authRepository.findByUserEntityEmail(userModel.email())).thenReturn(Optional.of(authEntity));

        AuthenticationResponseModel response = tokenService.reIssueAccessToken(request);

        assertNotNull(response);
        assertEquals("new-access-token", response.accessToken());
        assertEquals("new-refresh-token", response.refreshToken());
        verify(tokenRepository).save(any(TokenEntity.class));
    }

    @Test
    void reIssueAccessToken_MissingHeader() {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);

        ServiceException exception = assertThrows(ServiceException.class,
            () -> tokenService.reIssueAccessToken(request));
        assertEquals("Header is missing or invalid", exception.getMessage());
    }

    @Test
    void reIssueAccessToken_InvalidHeaderFormat() {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Invalid header");

        ServiceException exception = assertThrows(ServiceException.class,
            () -> tokenService.reIssueAccessToken(request));
        assertEquals("Header is missing or invalid", exception.getMessage());
    }

    @Test
    void reIssueAccessToken_InvalidToken() {
        String token = "invalid-token";
        String username = "testuser";
        ValidatedUserModel userModel = ValidatedUserModel.builder()
            .nickname(username)
            .build();

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + token);
        when(jwtService.extractUsername(token)).thenReturn(username);
        when(userService.findByNickname(username)).thenReturn(userModel);
        when(jwtService.isValidRefresh(token, userModel)).thenReturn(false);

        ServiceException exception = assertThrows(ServiceException.class,
            () -> tokenService.reIssueAccessToken(request));
        assertEquals("Invalid token", exception.getMessage());
    }

    @Test
    void reIssueRefreshToken_Success() {
        String refreshToken = "valid-refresh-token";
        String username = "testuser";
        RefreshTokenRequestModel request = new RefreshTokenRequestModel(refreshToken);
        ValidatedUserModel userModel = ValidatedUserModel.builder()
            .userId(1L)
            .nickname(username)
            .email("test@example.com")
            .build();

        AuthEntity authEntity = AuthEntity.builder()
            .tokens(new ArrayList<>())
            .build();

        when(jwtService.extractUsername(refreshToken)).thenReturn(username);
        when(userService.findByNickname(username)).thenReturn(userModel);
        when(jwtService.isValidRefresh(refreshToken, userModel)).thenReturn(true);
        when(jwtService.generateAccessToken(userModel)).thenReturn("new-access-token");
        when(jwtService.generateRefreshToken(userModel)).thenReturn("new-refresh-token");
        when(tokenRepository.findAllAccessTokenByUser(userModel.userId())).thenReturn(new ArrayList<>());
        when(authRepository.findByUserEntityEmail(userModel.email())).thenReturn(Optional.of(authEntity));

        AuthenticationResponseModel response = tokenService.reIssueRefreshToken(request);

        assertNotNull(response);
        assertEquals("new-access-token", response.accessToken());
        assertEquals("new-refresh-token", response.refreshToken());
    }

    @Test
    void reIssueRefreshToken_InvalidToken() {
        String refreshToken = "invalid-token";
        String username = "testuser";
        RefreshTokenRequestModel request = new RefreshTokenRequestModel(refreshToken);
        ValidatedUserModel userModel = ValidatedUserModel.builder()
            .nickname(username)
            .build();

        when(jwtService.extractUsername(refreshToken)).thenReturn(username);
        when(userService.findByNickname(username)).thenReturn(userModel);
        when(jwtService.isValidRefresh(refreshToken, userModel)).thenReturn(false);

        ServiceException exception = assertThrows(ServiceException.class,
            () -> tokenService.reIssueRefreshToken(request));
        assertEquals("Invalid token", exception.getMessage());
    }

    @Test
    void revokeAllTokensForUser_Success() {
        ValidatedUserModel userModel = ValidatedUserModel.builder()
            .userId(1L)
            .build();

        List<TokenEntity> tokens = List.of(
            TokenEntity.builder().isRevoke(false).build(),
            TokenEntity.builder().isRevoke(false).build()
        );

        when(tokenRepository.findAllAccessTokenByUser(userModel.userId())).thenReturn(tokens);

        tokenService.revokeAllTokensForUser(userModel);

        tokens.forEach(token -> assertTrue(token.getIsRevoke()));
        verify(tokenRepository).saveAll(tokens);
    }

    @Test
    void saveUserToken_Success() {
        ValidatedUserModel userModel = ValidatedUserModel.builder()
            .userId(1L)
            .email("test@example.com")
            .build();

        AuthEntity authEntity = AuthEntity.builder()
            .tokens(new ArrayList<>())
            .build();

        when(authRepository.findByUserEntityEmail(userModel.email())).thenReturn(Optional.of(authEntity));

        tokenService.saveUserToken("access-token", "refresh-token", userModel);

        verify(tokenRepository).save(any(TokenEntity.class));
        assertEquals(1, authEntity.getTokens().size());
    }

    @Test
    void saveUserToken_UserNotFound() {
        ValidatedUserModel userModel = ValidatedUserModel.builder()
            .email("test@example.com")
            .build();

        when(authRepository.findByUserEntityEmail(userModel.email())).thenReturn(Optional.empty());

        assertThrows(SerialException.class,
            () -> tokenService.saveUserToken("access-token", "refresh-token", userModel));
    }
}
