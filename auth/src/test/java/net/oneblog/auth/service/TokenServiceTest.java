package net.oneblog.auth.service;

import jakarta.servlet.http.HttpServletRequest;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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
    @Mock
    private TokenManagerService tokenManagerService;

    @Test
    void reIssueAccessToken_Success() {
        String token = "valid-refresh-token";
        String username = "testuser";
        ValidatedUserModel userModel = ValidatedUserModel.builder()
            .userId(1L)
            .nickname(username)
            .email("test@example.com")
            .build();

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + token);
        when(jwtService.extractUsername(token)).thenReturn(username);
        when(userService.findByNickname(username)).thenReturn(userModel);
        when(jwtService.isValidRefresh(token, userModel)).thenReturn(true);
        when(jwtService.generateAccessToken(userModel)).thenReturn("new-access-token");
        when(jwtService.generateRefreshToken(userModel)).thenReturn("new-refresh-token");

        AuthenticationResponseModel response = tokenService.reIssueAccessToken(request);

        assertNotNull(response);
        assertEquals("new-access-token", response.accessToken());
        assertEquals("new-refresh-token", response.refreshToken());
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
        RefreshTokenRequestModel refreshTokenRequest = new RefreshTokenRequestModel(refreshToken);
        ValidatedUserModel userModel = ValidatedUserModel.builder()
            .userId(1L)
            .nickname(username)
            .email("test@example.com")
            .build();

        when(jwtService.extractUsername(refreshToken)).thenReturn(username);
        when(userService.findByNickname(username)).thenReturn(userModel);
        when(jwtService.isValidRefresh(refreshToken, userModel)).thenReturn(true);
        when(jwtService.generateAccessToken(userModel)).thenReturn("new-access-token");
        when(jwtService.generateRefreshToken(userModel)).thenReturn("new-refresh-token");

        AuthenticationResponseModel response =
            tokenService.reIssueRefreshToken(refreshTokenRequest);

        assertNotNull(response);
        assertEquals("new-access-token", response.accessToken());
        assertEquals("new-refresh-token", response.refreshToken());
    }

    @Test
    void reIssueRefreshToken_InvalidToken() {
        String refreshToken = "invalid-token";
        String username = "testuser";
        RefreshTokenRequestModel refreshTokenRequest = new RefreshTokenRequestModel(refreshToken);
        ValidatedUserModel userModel = ValidatedUserModel.builder()
            .nickname(username)
            .build();

        when(jwtService.extractUsername(refreshToken)).thenReturn(username);
        when(userService.findByNickname(username)).thenReturn(userModel);
        when(jwtService.isValidRefresh(refreshToken, userModel)).thenReturn(false);

        ServiceException exception = assertThrows(ServiceException.class,
            () -> tokenService.reIssueRefreshToken(refreshTokenRequest));
        assertEquals("Invalid token", exception.getMessage());
    }
}
