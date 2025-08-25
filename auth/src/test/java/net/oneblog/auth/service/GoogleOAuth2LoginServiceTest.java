package net.oneblog.auth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import net.oneblog.auth.models.AuthModel;
import net.oneblog.auth.models.AuthenticationResponseModel;
import net.oneblog.auth.models.GoogleRegistrationRequestModel;
import net.oneblog.sharedexceptions.ServiceException;
import net.oneblog.user.service.UserService;
import net.oneblog.validationapi.models.ValidatedUserModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GoogleOAuth2LoginServiceTest {

    @InjectMocks
    private GoogleOAuth2LoginService googleOAuth2LoginService;

    @Mock
    private UserService userService;
    @Mock
    private AuthService authService;
    @Mock
    private JwtService jwtService;
    @Mock
    private TokenService tokenService;
    @Mock
    private GoogleIdTokenVerifier verifier;
    @Mock
    private GoogleIdToken googleIdToken;
    @Mock
    private GoogleIdToken.Payload payload;

    @Test
    void signUp_Success() {
        ValidatedUserModel userDto = ValidatedUserModel.builder()
            .email("john@example.com")
            .nickname("John")
            .name("John Doe")
            .build();

        AuthModel authModel = AuthModel.builder()
            .userDto(userDto)
            .build();

        when(payload.get("given_name")).thenReturn("John");
        when(payload.get("email")).thenReturn("john@example.com");
        when(payload.get("name")).thenReturn("John Doe");
        when(payload.getSubject()).thenReturn("google123");
        when(userService.existsByNickname("John")).thenReturn(false);
        when(authService.save(any(GoogleRegistrationRequestModel.class))).thenReturn(authModel);
        when(jwtService.generateAccessToken(userDto)).thenReturn("access-token");
        when(jwtService.generateRefreshToken(userDto)).thenReturn("refresh-token");

        AuthenticationResponseModel response = googleOAuth2LoginService.signUp(payload);

        assertNotNull(response);
        assertEquals("access-token", response.accessToken());
        assertEquals("refresh-token", response.refreshToken());
        verify(authService).save(any(GoogleRegistrationRequestModel.class));
        verify(tokenService).revokeAllTokensForUser(userDto);
        verify(tokenService).saveUserToken("access-token", "refresh-token", userDto);
    }

    @Test
    void signUp_NicknameExists_Success() {
        ValidatedUserModel userDto = ValidatedUserModel.builder()
            .email("john@example.com")
            .name("John Doe")
            .build();

        AuthModel authModel = AuthModel.builder()
            .userDto(userDto)
            .build();

        when(payload.get("given_name")).thenReturn("John");
        when(payload.get("email")).thenReturn("john@example.com");
        when(payload.get("name")).thenReturn("John Doe");
        when(payload.getSubject()).thenReturn("google123");
        when(userService.existsByNickname("John")).thenReturn(true);
        when(authService.save(any(GoogleRegistrationRequestModel.class))).thenReturn(authModel);
        when(jwtService.generateAccessToken(userDto)).thenReturn("access-token");
        when(jwtService.generateRefreshToken(userDto)).thenReturn("refresh-token");

        AuthenticationResponseModel response = googleOAuth2LoginService.signUp(payload);

        assertNotNull(response);
        verify(authService).save(any(GoogleRegistrationRequestModel.class));
    }

    @Test
    void login_Success() throws GeneralSecurityException, IOException {
        String token = "\"Bearer\" \"token\" \"value\" \"valid-token\"";
        ValidatedUserModel userDto = ValidatedUserModel.builder()
            .email("john@example.com")
            .build();

        AuthModel authModel = AuthModel.builder()
            .userDto(userDto)
            .build();

        when(verifier.verify(anyString())).thenReturn(googleIdToken);
        when(googleIdToken.getPayload()).thenReturn(payload);
        when(payload.getEmailVerified()).thenReturn(true);
        when(payload.getSubject()).thenReturn("google123");
        when(authService.findByGoogleUserId("google123")).thenReturn(authModel);
        when(jwtService.generateAccessToken(userDto)).thenReturn("access-token");
        when(jwtService.generateRefreshToken(userDto)).thenReturn("refresh-token");

        AuthenticationResponseModel response = googleOAuth2LoginService.login(token);

        assertNotNull(response);
        assertEquals("access-token", response.accessToken());
        assertEquals("refresh-token", response.refreshToken());
        verify(tokenService).revokeAllTokensForUser(userDto);
        verify(tokenService).saveUserToken("access-token", "refresh-token", userDto);
    }

    @Test
    void login_VerifyFailed() {
        String token = "invalid-token";

        assertThrows(ServiceException.class, () -> googleOAuth2LoginService.login(token));
    }

    @Test
    void verify_Success() throws GeneralSecurityException, IOException {
        String token = "\"Bearer\" \"token\" \"value\" \"valid-token\"";
        when(verifier.verify(anyString())).thenReturn(googleIdToken);
        when(googleIdToken.getPayload()).thenReturn(payload);
        when(payload.getEmailVerified()).thenReturn(true);

        GoogleIdToken.Payload result = googleOAuth2LoginService.verify(token);

        assertNotNull(result);
        assertEquals(payload, result);
    }

    @Test
    void verify_GoogleTokenNotExists() throws GeneralSecurityException, IOException {
        String token = "\"Bearer\" \"token\" \"value\" \"valid-token\"";
        when(verifier.verify(anyString())).thenReturn(null);

        assertThrows(GeneralSecurityException.class,
            () -> googleOAuth2LoginService.verify(token));
    }

    @Test
    void verify_EmailNotVerified() throws GeneralSecurityException, IOException {
        String token = "\"Bearer\" \"token\" \"value\" \"valid-token\"";
        when(verifier.verify(anyString())).thenReturn(googleIdToken);
        when(googleIdToken.getPayload()).thenReturn(payload);
        when(payload.getEmailVerified()).thenReturn(false);

        assertThrows(GeneralSecurityException.class,
            () -> googleOAuth2LoginService.verify(token));
    }

    @Test
    void verify_InvalidTokenFormat() {
        String token = "invalid-format";

        assertThrows(ServiceException.class,
            () -> googleOAuth2LoginService.login(token));
    }
}
