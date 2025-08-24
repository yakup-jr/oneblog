package net.oneblog.user;

import net.oneblog.api.dto.UserDto;
import net.oneblog.sharedexceptions.ServiceException;
import net.oneblog.user.models.UserCreateRequest;
import net.oneblog.user.entity.UserEntity;
import net.oneblog.user.exceptions.UserNotFoundException;
import net.oneblog.user.mappers.UserMapper;
import net.oneblog.user.repository.UserRepository;
import net.oneblog.user.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private static final UserEntity defaultUser = UserEntity.builder()
        .userId(1L)
        .name("Dima")
        .nickname("yakup_jr")
        .email("somemail@mail.com")
        .build();

    private final UserDto defaultUserDto = new UserDto(1L, "Dima", "yakup_jr", "somemail@mail.com");

    private static final UserCreateRequest DEFAULT_USER_CREATE_REQUEST = new UserCreateRequest(
        "Dima", "yakup_jr", "somemail@mail.com");

    @Test
    void save_ReturnUser() {
        when(userRepository.existsByNickname(DEFAULT_USER_CREATE_REQUEST.nickname())).thenReturn(false);
        when(userRepository.existsByEmail(DEFAULT_USER_CREATE_REQUEST.email())).thenReturn(false);
        when(userMapper.map(DEFAULT_USER_CREATE_REQUEST)).thenReturn(defaultUser);
        when(userRepository.save(defaultUser)).thenReturn(defaultUser);
        when(userMapper.map(defaultUser)).thenReturn(defaultUserDto);

        UserDto savedUser = userService.save(DEFAULT_USER_CREATE_REQUEST);

        assertThat(savedUser).isNotNull().isInstanceOf(UserDto.class);
        assertThat(savedUser).isEqualTo(defaultUserDto);
        assertThat(savedUser.userId()).isEqualTo(defaultUserDto.userId());
    }

    @Test
    void save_ThrowApiRequestException_NicknameExists() {
        when(userRepository.existsByNickname(DEFAULT_USER_CREATE_REQUEST.nickname())).thenReturn(true);

        assertThatThrownBy(() -> userService.save(DEFAULT_USER_CREATE_REQUEST)).isInstanceOf(
            ServiceException.class);
    }

    @Test
    void save_ThrowApiRequestException_EmailExists() {
        when(userRepository.existsByNickname(DEFAULT_USER_CREATE_REQUEST.nickname())).thenReturn(false);
        when(userRepository.existsByEmail(DEFAULT_USER_CREATE_REQUEST.email())).thenReturn(true);

        assertThatThrownBy(() -> userService.save(DEFAULT_USER_CREATE_REQUEST)).isInstanceOf(
            ServiceException.class);
    }

    @Test
    void existsById_ReturnTrue() {
        when(userRepository.existsById(1L)).thenReturn(true);

        boolean userExists = userService.existsById(1L);

        assertThat(userExists).isTrue();
    }

    @Test
    void existsById_ReturnFalse() {
        when(userRepository.existsById(999L)).thenReturn(false);

        boolean userExists = userService.existsById(999L);

        assertThat(userExists).isFalse();
    }

    @Test
    void findById_ReturnUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(defaultUser));
        when(userMapper.map(defaultUser)).thenReturn(defaultUserDto);

        UserDto responseUser = userService.findById(1L);

        assertThat(responseUser).isNotNull().isInstanceOf(UserDto.class);
        assertThat(responseUser.userId()).isEqualTo(1L);
        assertThat(responseUser).isEqualTo(defaultUserDto);
    }

    @Test
    void findById_ThrowUserNotFoundException() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findById(999L)).isInstanceOf(
            UserNotFoundException.class);
    }

    @Test
    void findByNickname_ReturnUser() {
        when(userRepository.findByNickname(defaultUser.getNickname())).thenReturn(
            Optional.of(defaultUser));
        when(userMapper.map(defaultUser)).thenReturn(defaultUserDto);

        UserDto responseUser = userService.findByNickname(defaultUser.getNickname());

        assertThat(responseUser).isNotNull().isInstanceOf(UserDto.class);
        assertThat(responseUser.userId()).isEqualTo(defaultUser.getUserId());
        assertThat(responseUser).isEqualTo(defaultUserDto);
    }

    @Test
    void findByNickname_ThrowUserNotFoundException() {
        when(userRepository.findByNickname(defaultUser.getNickname())).thenReturn(Optional.empty());

        assertThatThrownBy(
            () -> userService.findByNickname(defaultUser.getNickname())).isInstanceOf(
            UserNotFoundException.class);
    }

    @Test
    void findByEmail_ReturnUser() {
        when(userRepository.findByEmail(defaultUser.getEmail())).thenReturn(
            Optional.of(defaultUser));
        when(userMapper.map(defaultUser)).thenReturn(defaultUserDto);

        UserDto responseUser = userService.findByEmail(defaultUser.getEmail());

        assertThat(responseUser).isNotNull().isInstanceOf(UserDto.class);
        assertThat(responseUser.userId()).isEqualTo(defaultUser.getUserId());
        assertThat(responseUser).isEqualTo(defaultUserDto);
    }

    @Test
    void findByEmail_ThrowUserNotFoundException() {
        when(userRepository.findByEmail(defaultUser.getEmail())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findByEmail(defaultUser.getEmail())).isInstanceOf(
            UserNotFoundException.class);
    }
}