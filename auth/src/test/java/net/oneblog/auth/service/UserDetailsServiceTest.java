package net.oneblog.auth.service;

import net.oneblog.auth.adapter.AuthAdapter;
import net.oneblog.user.entity.UserEntity;
import net.oneblog.user.exceptions.UserNotFoundException;
import net.oneblog.user.mappers.UserMapper;
import net.oneblog.user.service.UserService;
import net.oneblog.validationapi.mappers.ValidatedUserModelMapper;
import net.oneblog.validationapi.models.ValidatedUserModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceTest {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;
    @Mock
    private UserService userService;
    @Mock
    private UserMapper userMapper;
    @Mock
    private ValidatedUserModelMapper validatedUserModelMapper;

    @Test
    void loadUserByUsername_Success() {
        String username = "testuser";

        ValidatedUserModel userModel = ValidatedUserModel.builder()
            .userId(1L)
            .nickname(username)
            .email("test@example.com")
            .build();

        when(userService.findByNickname(username)).thenReturn(userModel);
        when(userMapper.map(userModel)).thenReturn(new UserEntity(1L, "testname", username, "test@example.com"));

        UserDetails result = userDetailsService.loadUserByUsername(username);

        assertNotNull(result);
        assertInstanceOf(AuthAdapter.class, result);
        assertEquals(username, result.getUsername());
    }

    @Test
    void loadUserByUsername_UserNotFound() {
        String username = "nonexistent";

        when(userService.findByNickname(username)).thenThrow(
            new UserNotFoundException("User with nickname " + username + " not found"));

        assertThrows(UserNotFoundException.class,
            () -> userDetailsService.loadUserByUsername(username));
    }

    @Test
    void loadUserByUsername_NullUsername() {
        String username = null;

        when(userService.findByNickname(username)).thenThrow(
            new UserNotFoundException("User with nickname null not found"));

        assertThrows(UserNotFoundException.class,
            () -> userDetailsService.loadUserByUsername(username));
    }

    @Test
    void loadUserByUsername_EmptyUsername() {
        String username = "";

        when(userService.findByNickname(username)).thenThrow(
            new UserNotFoundException("User with nickname  not found"));

        assertThrows(UserNotFoundException.class,
            () -> userDetailsService.loadUserByUsername(username));
    }
}
