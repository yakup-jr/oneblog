package net.oneblog.auth.service;

import net.oneblog.auth.entity.AuthEntity;
import net.oneblog.auth.mapper.AuthMapper;
import net.oneblog.auth.models.AuthModel;
import net.oneblog.auth.repository.AuthRepository;
import net.oneblog.sharedexceptions.ServiceException;
import net.oneblog.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthRepository authRepository;
    @Mock
    private AuthMapper authMapper;
    @Mock
    private UserService userService;

    @InjectMocks
    private AuthServiceImpl authService;

    private AuthEntity authEntity;
    private AuthModel authModel;
    private final String testEmail = "test@example.com";
    private final String testGoogleUserId = "google123";

    @BeforeEach
    void setUp() {
        authEntity =
            AuthEntity.builder().authId(1L).verificated(true).googleUserId(testGoogleUserId)
                .build();
        authModel =
            AuthModel.builder().authId(1L).verificated(true).googleUserId(testGoogleUserId).build();
    }

    @Test
    void findByEmail_ShouldReturnAuthModel_WhenEmailExists() {
        when(authRepository.findByUserEntityEmail(testEmail)).thenReturn(Optional.of(authEntity));
        when(authMapper.map(authEntity)).thenReturn(authModel);

        AuthModel result = authService.findByEmail(testEmail);

        assertNotNull(result);
        verify(authRepository).findByUserEntityEmail(testEmail);
    }

    @Test
    void findByEmail_ShouldThrowServiceException_WhenEmailNotFound() {
        when(authRepository.findByUserEntityEmail(testEmail)).thenReturn(Optional.empty());

        ServiceException exception = assertThrows(ServiceException.class,
            () -> authService.findByEmail(testEmail));

        assertTrue(exception.getMessage().contains("not found"));
        verify(authRepository).findByUserEntityEmail(testEmail);
    }

    @Test
    void findByGoogleUserId_ShouldReturnAuthModel_WhenGoogleUserIdExists() {
        when(authRepository.findByGoogleUserId(testGoogleUserId)).thenReturn(
            Optional.of(authEntity));
        when(authMapper.map(authEntity)).thenReturn(authModel);

        AuthModel result = authService.findByGoogleUserId(testGoogleUserId);

        assertNotNull(result);
        verify(authRepository).findByGoogleUserId(testGoogleUserId);
    }

    @Test
    void findByGoogleUserId_ShouldThrowServiceException_WhenGoogleUserIdNotFound() {
        when(authRepository.findByGoogleUserId(testGoogleUserId)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class,
            () -> authService.findByGoogleUserId(testGoogleUserId));

        verify(authRepository).findByGoogleUserId(testGoogleUserId);
    }
}
