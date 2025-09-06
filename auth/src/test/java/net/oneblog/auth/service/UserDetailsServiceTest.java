package net.oneblog.auth.service;

import net.oneblog.auth.adapter.AuthAdapter;
import net.oneblog.auth.entity.AuthEntity;
import net.oneblog.auth.repository.AuthRepository;
import net.oneblog.sharedexceptions.ServiceException;
import net.oneblog.user.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceTest {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;
    @Mock
    private AuthRepository authRepository;

    @Test
    void loadUserByUsername_Success() {
        String username = "testuser";

        AuthEntity authEntity = AuthEntity.builder()
            .authId(1L)
            .userEntity(new UserEntity(1L, "name", username, "test@example.com"))
            .build();

        when(authRepository.findByNickname(username)).thenReturn(Optional.ofNullable(authEntity));

        UserDetails result = userDetailsService.loadUserByUsername(username);

        assertNotNull(result);
        assertInstanceOf(AuthAdapter.class, result);
        assertEquals(username, result.getUsername());
    }

    @Test
    void loadUserByUsername_UserNotFound() {
        String username = "nonexistent";

        when(authRepository.findByNickname(username)).thenThrow(
            new ServiceException("User with nickname " + username + " not found"));

        assertThrows(ServiceException.class,
            () -> userDetailsService.loadUserByUsername(username));
    }

    @Test
    void loadUserByUsername_NullUsername() {
        String username = null;

        when(authRepository.findByNickname(username)).thenThrow(
            new ServiceException("User with nickname null not found"));

        assertThrows(ServiceException.class,
            () -> userDetailsService.loadUserByUsername(username));
    }

    @Test
    void loadUserByUsername_EmptyUsername() {
        String username = "";

        when(authRepository.findByNickname(username)).thenThrow(
            new ServiceException("User with nickname  not found"));

        assertThrows(ServiceException.class,
            () -> userDetailsService.loadUserByUsername(username));
    }
}
