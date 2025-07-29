package net.oneblog.auth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import net.oneblog.auth.models.AuthenticationResponseModel;
import net.oneblog.auth.entity.AuthEntity;
import net.oneblog.auth.repository.AuthRepository;
import net.oneblog.user.entity.UserEntity;
import net.oneblog.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
    private UserRepository userRepository;
    @Mock
    private AuthRepository authRepository;
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
    void sighUp_Success() {
        Map<String, Object> payloadData = new HashMap<>();
        payloadData.put("given_name", "John");
        payloadData.put("email", "john@example.com");
        payloadData.put("name", "John Doe");

        when(payload.get("given_name")).thenReturn("John");
        when(payload.get("email")).thenReturn("john@example.com");
        when(payload.get("name")).thenReturn("John Doe");
        when(payload.getSubject()).thenReturn("google123");
        when(userRepository.findByNickname("John")).thenReturn(Optional.empty());

        AuthEntity savedAuthEntity = AuthEntity.builder()
            .userEntity(UserEntity.builder().build())
            .googleUserId("google123")
            .build();
        when(authRepository.save(any(AuthEntity.class))).thenReturn(savedAuthEntity);
        when(jwtService.generateAccessToken(any())).thenReturn("access-token");
        when(jwtService.generateRefreshToken(any())).thenReturn("refresh-token");

        AuthenticationResponseModel response = googleOAuth2LoginService.signUp(payload);

        assertNotNull(response);
        assertEquals("access-token", response.accessToken());
        assertEquals("refresh-token", response.refreshToken());
        verify(authRepository).save(any(AuthEntity.class));
        verify(tokenService).revokeAllTokensForUser(any());
        verify(tokenService).saveUserToken(anyString(), anyString(), any());
    }

    @Test
    void sighUp_NicknameExists_Success() {
        when(payload.get("given_name")).thenReturn("John");
        when(payload.get("email")).thenReturn("john@example.com");
        when(payload.get("name")).thenReturn("John Doe");
        when(payload.getSubject()).thenReturn("google123");
        when(userRepository.findByNickname("John")).thenReturn(Optional.of(new UserEntity()));

        AuthEntity savedAuthEntity = AuthEntity.builder()
            .userEntity(UserEntity.builder().build())
            .googleUserId("google123")
            .build();
        when(authRepository.save(any(AuthEntity.class))).thenReturn(savedAuthEntity);
        when(jwtService.generateAccessToken(any())).thenReturn("access-token");
        when(jwtService.generateRefreshToken(any())).thenReturn("refresh-token");

        AuthenticationResponseModel response = googleOAuth2LoginService.signUp(payload);

        assertNotNull(response);
        verify(authRepository).save(any(AuthEntity.class));
    }

    @Test
    void login_VerifyFailed() {
        String token = "invalid-token";

        assertThrows(GeneralSecurityException.class, () -> googleOAuth2LoginService.login(token));
    }

    @Test
    void login_foundByGoogleId() throws GeneralSecurityException, IOException {
        String token = "\"Bearer\" \"token\" \"value\" \"valid-token\"";
        UserEntity userEntity = new UserEntity();

        when(verifier.verify(anyString())).thenReturn(googleIdToken);
        when(googleIdToken.getPayload()).thenReturn(payload);
        when(payload.getEmailVerified()).thenReturn(true);
        when(payload.get("email")).thenReturn("john@example.com");
        when(payload.getSubject()).thenReturn("google123");
        when(authRepository.findByGoogleUserId("google123")).thenReturn(Optional.of(userEntity));
        when(jwtService.generateAccessToken(userEntity)).thenReturn("access-token");
        when(jwtService.generateRefreshToken(userEntity)).thenReturn("refresh-token");

        AuthenticationResponseModel response = googleOAuth2LoginService.login(token);

        assertNotNull(response);
        assertEquals("access-token", response.accessToken());
        assertEquals("refresh-token", response.refreshToken());
        verify(tokenService).revokeAllTokensForUser(userEntity);
        verify(tokenService).saveUserToken("access-token", "refresh-token", userEntity);
    }

    @Test
    void login_foundByEmail() throws GeneralSecurityException, IOException {
        String token = "\"Bearer\" \"token\" \"value\" \"valid-token\"";
        AuthEntity authEntity = AuthEntity.builder()
            .userEntity(UserEntity.builder().build())
            .build();

        when(verifier.verify(anyString())).thenReturn(googleIdToken);
        when(googleIdToken.getPayload()).thenReturn(payload);
        when(payload.getEmailVerified()).thenReturn(true);
        when(payload.get("email")).thenReturn("john@example.com");
        when(payload.getSubject()).thenReturn("google123");
        when(authRepository.findByGoogleUserId("google123")).thenReturn(Optional.empty());
        when(authRepository.findByEmail("john@example.com")).thenReturn(Optional.of(authEntity));
        when(authRepository.save(authEntity)).thenReturn(authEntity);
        when(jwtService.generateAccessToken(any())).thenReturn("access-token");
        when(jwtService.generateRefreshToken(any())).thenReturn("refresh-token");

        AuthenticationResponseModel response = googleOAuth2LoginService.login(token);

        assertNotNull(response);
        assertEquals("google123", authEntity.getGoogleUserId());
        verify(authRepository).save(authEntity);
    }

    @Test
    void login_UserNotFound() throws GeneralSecurityException, IOException {
        String token = "\"Bearer\" \"token\" \"value\" \"valid-token\"";

        when(verifier.verify(anyString())).thenReturn(googleIdToken);
        when(googleIdToken.getPayload()).thenReturn(payload);
        when(payload.getEmailVerified()).thenReturn(true);
        when(payload.get("email")).thenReturn("john@example.com");
        when(payload.getSubject()).thenReturn("google123");
        when(authRepository.findByGoogleUserId("google123")).thenReturn(Optional.empty());
        when(authRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());

        when(payload.get("given_name")).thenReturn("John");
        when(payload.get("name")).thenReturn("John Doe");
        when(userRepository.findByNickname("John")).thenReturn(Optional.empty());
        AuthEntity savedAuthEntity = AuthEntity.builder()
            .userEntity(UserEntity.builder().build())
            .build();
        when(authRepository.save(any(AuthEntity.class))).thenReturn(savedAuthEntity);
        when(jwtService.generateAccessToken(any())).thenReturn("access-token");
        when(jwtService.generateRefreshToken(any())).thenReturn("refresh-token");

        AuthenticationResponseModel response = googleOAuth2LoginService.login(token);

        assertNotNull(response);
        verify(authRepository).save(any(AuthEntity.class));
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
    void verify_EmailVerificationFailed() throws GeneralSecurityException, IOException {
        String token = "\"Bearer\" \"token\" \"value\" \"valid-token\"";
        when(verifier.verify(anyString())).thenReturn(googleIdToken);
        when(googleIdToken.getPayload()).thenReturn(payload);
        when(payload.getEmailVerified()).thenReturn(false);

        assertThrows(GeneralSecurityException.class, () -> googleOAuth2LoginService.verify(token));
    }
}
