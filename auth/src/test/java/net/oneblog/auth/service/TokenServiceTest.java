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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @InjectMocks
    private TokenServiceImpl tokenService;

    @Mock
    private JwtService jwtService;
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
        UserEntity userEntity = UserEntity.builder()
            .userId(1L)
            .nickname(username)
            .email("test@example.com")
            .build();

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + token);
        when(jwtService.extractUsername(token)).thenReturn(username);
        when(userRepository.findByNickname(username)).thenReturn(Optional.of(userEntity));
        when(jwtService.isValidRefresh(token, userEntity)).thenReturn(true);
        when(jwtService.generateAccessToken(userEntity)).thenReturn("new-access-token");
        when(jwtService.generateRefreshToken(userEntity)).thenReturn("new-refresh-token");
        when(tokenRepository.findAllAccessTokenByUser(userEntity.getUserId())).thenReturn(
            new ArrayList<>());

        AuthEntity authEntity = AuthEntity.builder()
            .userEntity(userEntity)
            .tokens(new ArrayList<>())
            .build();
        when(authRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.of(authEntity));

        AuthenticationResponseDto response = tokenService.reIssueAccessToken(request);

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
    void reIssueAccessToken_UserNotFound() {
        String token = "valid-token";
        String username = "nonexistent";

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + token);
        when(jwtService.extractUsername(token)).thenReturn(username);
        when(userRepository.findByNickname(username)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
            () -> tokenService.reIssueAccessToken(request));
    }

    @Test
    void reIssueAccessToken_InvalidToken() {
        String token = "invalid-token";
        String username = "testuser";
        UserEntity userEntity = UserEntity.builder()
            .nickname(username)
            .build();

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + token);
        when(jwtService.extractUsername(token)).thenReturn(username);
        when(userRepository.findByNickname(username)).thenReturn(Optional.of(userEntity));
        when(jwtService.isValidRefresh(token, userEntity)).thenReturn(false);

        ServiceException exception = assertThrows(ServiceException.class,
            () -> tokenService.reIssueAccessToken(request));
        assertEquals("Invalid token", exception.getMessage());
    }

    @Test
    void reIssueRefreshToken_Success() {
        String refreshToken = "valid-refresh-token";
        String username = "testuser";
        RefreshTokenRequestDto request = new RefreshTokenRequestDto(refreshToken);
        UserEntity userEntity = UserEntity.builder()
            .userId(1L)
            .nickname(username)
            .email("test@example.com")
            .build();

        when(jwtService.extractUsername(refreshToken)).thenReturn(username);
        when(userRepository.findByNickname(username)).thenReturn(Optional.of(userEntity));
        when(jwtService.isValidRefresh(refreshToken, userEntity)).thenReturn(true);
        when(jwtService.generateAccessToken(userEntity)).thenReturn("new-access-token");
        when(jwtService.generateRefreshToken(userEntity)).thenReturn("new-refresh-token");
        when(tokenRepository.findAllAccessTokenByUser(userEntity.getUserId())).thenReturn(
            new ArrayList<>());

        AuthEntity authEntity = AuthEntity.builder()
            .userEntity(userEntity)
            .tokens(new ArrayList<>())
            .build();
        when(authRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.of(authEntity));

        AuthenticationResponseDto response = tokenService.reIssueRefreshToken(request);

        assertNotNull(response);
        assertEquals("new-access-token", response.accessToken());
        assertEquals("new-refresh-token", response.refreshToken());
    }

    @Test
    void reIssueRefreshToken_UserNotFound() {
        String refreshToken = "valid-token";
        String username = "nonexistent";
        RefreshTokenRequestDto request = new RefreshTokenRequestDto(refreshToken);

        when(jwtService.extractUsername(refreshToken)).thenReturn(username);
        when(userRepository.findByNickname(username)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
            () -> tokenService.reIssueRefreshToken(request));
    }

    @Test
    void reIssueRefreshToken_InvalidToken() {
        String refreshToken = "invalid-token";
        String username = "testuser";
        RefreshTokenRequestDto request = new RefreshTokenRequestDto(refreshToken);
        UserEntity userEntity = UserEntity.builder()
            .nickname(username)
            .build();

        when(jwtService.extractUsername(refreshToken)).thenReturn(username);
        when(userRepository.findByNickname(username)).thenReturn(Optional.of(userEntity));
        when(jwtService.isValidRefresh(refreshToken, userEntity)).thenReturn(false);

        ServiceException exception = assertThrows(ServiceException.class,
            () -> tokenService.reIssueRefreshToken(request));
        assertEquals("Invalid token", exception.getMessage());
    }

    @Test
    void revokeAllTokensForUser_Success() {
        UserEntity userEntity = UserEntity.builder()
            .userId(1L)
            .build();

        List<TokenEntity> tokens = List.of(
            TokenEntity.builder().isRevoke(false).build(),
            TokenEntity.builder().isRevoke(false).build()
        );

        when(tokenRepository.findAllAccessTokenByUser(userEntity.getUserId())).thenReturn(tokens);

        tokenService.revokeAllTokensForUser(userEntity);

        tokens.forEach(token -> assertTrue(token.getIsRevoke()));
        verify(tokenRepository).saveAll(tokens);
    }

    @Test
    void revokeAllTokensForUser_NoTokens() {
        UserEntity userEntity = UserEntity.builder()
            .userId(1L)
            .build();

        when(tokenRepository.findAllAccessTokenByUser(userEntity.getUserId())).thenReturn(
            new ArrayList<>());

        tokenService.revokeAllTokensForUser(userEntity);

        verify(tokenRepository).saveAll(any());
    }

    @Test
    void saveUserToken_Success() {
        String accessToken = "access-token";
        String refreshToken = "refresh-token";
        UserEntity userEntity = UserEntity.builder()
            .email("test@example.com")
            .build();

        AuthEntity authEntity = AuthEntity.builder()
            .userEntity(userEntity)
            .tokens(new ArrayList<>())
            .build();

        when(authRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.of(authEntity));

        tokenService.saveUserToken(accessToken, refreshToken, userEntity);

        verify(tokenRepository).save(any(TokenEntity.class));
        assertEquals(1, authEntity.getTokens().size());
    }

    @Test
    void saveUserToken_UserNotFound() {
        String accessToken = "access-token";
        String refreshToken = "refresh-token";
        UserEntity userEntity = UserEntity.builder()
            .email("nonexistent@example.com")
            .build();

        when(authRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.empty());

        assertThrows(SerialException.class,
            () -> tokenService.saveUserToken(accessToken, refreshToken, userEntity));
    }
}
