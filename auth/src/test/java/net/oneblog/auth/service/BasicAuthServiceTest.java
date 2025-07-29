package net.oneblog.auth.service;

import net.oneblog.api.interfaces.RoleNameDomain;
import net.oneblog.auth.models.AuthenticationResponseModel;
import net.oneblog.auth.models.LoginRequestModel;
import net.oneblog.auth.models.RegistrationRequestModel;
import net.oneblog.auth.entity.AuthEntity;
import net.oneblog.auth.entity.RoleEntity;
import net.oneblog.auth.repository.AuthRepository;
import net.oneblog.auth.repository.RoleRepository;
import net.oneblog.email.models.RegistrationEmailVerificationModel;
import net.oneblog.email.exceptions.InvalidVerificationCodeException;
import net.oneblog.email.service.EmailVerificationService;
import net.oneblog.sharedexceptions.ServiceException;
import net.oneblog.user.entity.UserEntity;
import net.oneblog.user.exceptions.UserNotFoundException;
import net.oneblog.user.repository.UserRepository;
import net.oneblog.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

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
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserService userService;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private EmailVerificationService emailVerificationService;
    @Mock
    private AuthRepository authRepository;
    @Mock
    private TokenService tokenService;

    @Test
    void register_Success() {
        RegistrationRequestModel request = new RegistrationRequestModel("name",
            "testuser", "test@example.com", "password123");

        RoleEntity userRole = new RoleEntity();
        userRole.setName(RoleNameDomain.ROLE_USER);

        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(roleRepository.findByName(RoleNameDomain.ROLE_USER)).thenReturn(Optional.of(userRole));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        basicAuthService.register(request);

        verify(emailVerificationService).sendVerificationCode(request.email());
    }

    @Test
    void register_UserAlreadyExists() {
        RegistrationRequestModel request = new RegistrationRequestModel("name",
            "testuser", "existing@example.com", "password123");

        when(userRepository.existsByEmail(request.email())).thenReturn(true);

        assertThrows(ServiceException.class, () -> basicAuthService.register(request));
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    void verifyEmail_Success() {
        RegistrationEmailVerificationModel request = new RegistrationEmailVerificationModel(
            "test@example.com", "123456");

        AuthEntity authEntity = new AuthEntity();

        when(emailVerificationService.verifyCode(request)).thenReturn(true);
        when(authRepository.findByEmail(request.email())).thenReturn(Optional.of(authEntity));

        basicAuthService.verifyEmail(request);

        assertTrue(authEntity.isVerificated());
        verify(authRepository).save(authEntity);
    }

    @Test
    void verifyEmail_InvalidCode() {
        RegistrationEmailVerificationModel request = new RegistrationEmailVerificationModel(
            "test@example.com", "invalid");

        when(emailVerificationService.verifyCode(request)).thenReturn(false);

        assertThrows(InvalidVerificationCodeException.class,
            () -> basicAuthService.verifyEmail(request));

        verify(authRepository, never()).findByEmail(anyString());
        verify(authRepository, never()).save(any());
    }

    @Test
    void authenticate_Success() {
        LoginRequestModel request = new LoginRequestModel("testuser", "password123");
        UserEntity userEntity = new UserEntity();

        when(userRepository.findByNickname(request.username())).thenReturn(
            Optional.of(userEntity));
        when(jwtService.generateAccessToken(userEntity)).thenReturn("access-token");
        when(jwtService.generateRefreshToken(userEntity)).thenReturn("refresh-token");

        AuthenticationResponseModel response = basicAuthService.authenticate(request);

        assertNotNull(response);
        assertEquals("access-token", response.accessToken());
        assertEquals("refresh-token", response.refreshToken());

        verify(authenticationManager).authenticate(
            new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        verify(tokenService).revokeAllTokensForUser(userEntity);
        verify(tokenService).saveUserToken("access-token", "refresh-token", userEntity);
    }

    @Test
    void authenticate_UserNotFound() {
        LoginRequestModel request = new LoginRequestModel("unknown", "password123");

        when(userRepository.findByNickname(request.username())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> basicAuthService.authenticate(request));

        verify(authenticationManager).authenticate(
            new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        verify(tokenService, never()).revokeAllTokensForUser(any());
        verify(tokenService, never()).saveUserToken(anyString(), anyString(), any());
    }
}
