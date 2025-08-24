package net.oneblog.auth.service;

import net.oneblog.api.interfaces.RoleNameDomain;
import net.oneblog.auth.entity.RoleEntity;
import net.oneblog.auth.models.AuthModel;
import net.oneblog.auth.models.AuthenticationResponseModel;
import net.oneblog.auth.models.BasicRegistrationRequestModel;
import net.oneblog.auth.models.LoginRequestModel;
import net.oneblog.email.exceptions.InvalidVerificationCodeException;
import net.oneblog.email.models.RegistrationEmailVerificationModel;
import net.oneblog.email.service.EmailVerificationService;
import net.oneblog.sharedexceptions.ServiceException;
import net.oneblog.user.service.UserService;
import net.oneblog.validationapi.models.ValidatedUserModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasicAuthServiceTest {

    @InjectMocks
    private BasicAuthServiceImpl basicAuthService;

    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserService userService;
    @Mock
    private EmailVerificationService emailVerificationService;
    @Mock
    private AuthService authService;
    @Mock
    private TokenService tokenService;

    @Test
    void register_Success() {
        BasicRegistrationRequestModel request = new BasicRegistrationRequestModel("name",
            "testuser", "test@example.com", "password123");
        AuthModel authModel = AuthModel.builder().build();

        when(authService.save(any(BasicRegistrationRequestModel.class))).thenReturn(authModel);

        basicAuthService.register(request);

        verify(authService).save(any(BasicRegistrationRequestModel.class));
        verify(emailVerificationService).sendVerificationCode(request.email());
    }

    @Test
    void register_EmailAlreadyExists() {
        BasicRegistrationRequestModel request = new BasicRegistrationRequestModel("name",
            "testuser", "existing@example.com", "password123");

        when(authService.save(request)).thenThrow(new ServiceException("email already exists"));

        assertThrows(ServiceException.class, () -> basicAuthService.register(request));
    }

    @Test
    void register_UsernameAlreadyExists() {
        BasicRegistrationRequestModel request =
            new BasicRegistrationRequestModel("name", "existinguser", "test@example.com",
                "password123");

        when(authService.save(request)).thenThrow(new ServiceException("username already exists"));

        assertThrows(ServiceException.class, () -> basicAuthService.register(request));
    }

    @Test
    void verifyEmail_Success() {
        RegistrationEmailVerificationModel request = new RegistrationEmailVerificationModel(
            "test@example.com", "123456");

        AuthModel authModel = AuthModel.builder()
            .verificated(false)
            .build();

        when(emailVerificationService.verifyCode(request)).thenReturn(true);
        when(authService.findByEmail(request.email())).thenReturn(authModel);

        basicAuthService.verifyEmail(request);

        assertTrue(authModel.isVerificated());
        verify(authService).update(authModel);
    }

    @Test
    void verifyEmail_InvalidCode() {
        RegistrationEmailVerificationModel request = new RegistrationEmailVerificationModel(
            "test@example.com", "invalid");

        when(emailVerificationService.verifyCode(request)).thenReturn(false);

        assertThrows(InvalidVerificationCodeException.class,
            () -> basicAuthService.verifyEmail(request));

        verify(authService, never()).findByEmail(anyString());
        verify(authService, never()).save((BasicRegistrationRequestModel) any());
    }

    @Test
    void authenticate_Success() {
        LoginRequestModel request = new LoginRequestModel("testuser", "password123");
        ValidatedUserModel userModel = ValidatedUserModel.builder()
            .userId(1L)
            .name("testname")
            .nickname("testuser")
            .email("test@example.com")
            .build();

        when(userService.findByNickname(request.username())).thenReturn(userModel);
        when(jwtService.generateAccessToken(userModel)).thenReturn("access-token");
        when(jwtService.generateRefreshToken(userModel)).thenReturn("refresh-token");

        AuthenticationResponseModel response = basicAuthService.authenticate(request);

        assertNotNull(response);
        assertEquals("access-token", response.accessToken());
        assertEquals("refresh-token", response.refreshToken());

        verify(authenticationManager).authenticate(
            new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        verify(tokenService).revokeAllTokensForUser(userModel);
        verify(tokenService).saveUserToken("access-token", "refresh-token", userModel);
    }

    @Test
    void authenticate_UserNotFound() {
        LoginRequestModel request = new LoginRequestModel("unknown", "password123");

        when(userService.findByNickname(request.username())).thenThrow(
            new RuntimeException("User not found"));

        assertThrows(RuntimeException.class, () -> basicAuthService.authenticate(request));

        verify(authenticationManager).authenticate(
            new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        verify(tokenService, never()).revokeAllTokensForUser(any());
        verify(tokenService, never()).saveUserToken(anyString(), anyString(), any());
    }
}
