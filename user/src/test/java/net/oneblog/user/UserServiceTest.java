package net.oneblog.user;

import net.oneblog.sharedexceptions.ServiceException;
import net.oneblog.user.dto.UserCreateDto;
import net.oneblog.user.dto.UserDto;
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

    private static final UserDto defaultUserDto = UserDto.builder()
        .userId(1L)
        .name("Dima")
        .nickname("yakup_jr")
        .email("somemail@mail.com")
        .build();

    private static final UserCreateDto defaultUserCreateDto = new UserCreateDto(
        "Dima", "yakup_jr", "somemail@mail.com");

    @Test
    void save_ReturnUser() {
        // Arrange
        when(userRepository.existsByNickname(defaultUserCreateDto.getNickname())).thenReturn(false);
        when(userRepository.existsByEmail(defaultUserCreateDto.getEmail())).thenReturn(false);
        when(userMapper.map(defaultUserCreateDto)).thenReturn(defaultUser);
        when(userRepository.save(defaultUser)).thenReturn(defaultUser);
        when(userMapper.map(defaultUser)).thenReturn(defaultUserDto);

        // Act
        UserDto savedUser = userService.save(defaultUserCreateDto);

        // Assert
        assertThat(savedUser).isNotNull().isInstanceOf(UserDto.class);
        assertThat(savedUser.getUserId()).isEqualTo(defaultUserDto.getUserId());
        assertThat(savedUser).isEqualTo(defaultUserDto);
    }

    @Test
    void save_ThrowApiRequestException_NicknameExists() {
        // Arrange
        when(userRepository.existsByNickname(defaultUserCreateDto.getNickname())).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> userService.save(defaultUserCreateDto)).isInstanceOf(
            ServiceException.class);
    }

    @Test
    void save_ThrowApiRequestException_EmailExists() {
        // Arrange
        when(userRepository.existsByNickname(defaultUserCreateDto.getNickname())).thenReturn(false);
        when(userRepository.existsByEmail(defaultUserCreateDto.getEmail())).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> userService.save(defaultUserCreateDto)).isInstanceOf(
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
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(defaultUser));
        when(userMapper.map(defaultUser)).thenReturn(defaultUserDto);

        // Act
        UserDto responseUser = userService.findById(1L);

        // Assert
        assertThat(responseUser).isNotNull().isInstanceOf(UserDto.class);
        assertThat(responseUser.getUserId()).isEqualTo(1L);
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
        // Arrange
        when(userRepository.findByNickname(defaultUser.getNickname())).thenReturn(
            Optional.of(defaultUser));
        when(userMapper.map(defaultUser)).thenReturn(defaultUserDto);

        // Act
        UserDto responseUser = userService.findByNickname(defaultUser.getNickname());

        // Assert
        assertThat(responseUser).isNotNull().isInstanceOf(UserDto.class);
        assertThat(responseUser.getUserId()).isEqualTo(defaultUser.getUserId());
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
        // Arrange
        when(userRepository.findByEmail(defaultUser.getEmail())).thenReturn(
            Optional.of(defaultUser));
        when(userMapper.map(defaultUser)).thenReturn(defaultUserDto);

        // Act
        UserDto responseUser = userService.findByEmail(defaultUser.getEmail());

        // Assert
        assertThat(responseUser).isNotNull().isInstanceOf(UserDto.class);
        assertThat(responseUser.getUserId()).isEqualTo(defaultUser.getUserId());
        assertThat(responseUser).isEqualTo(defaultUserDto);
    }

    @Test
    void findByEmail_ThrowUserNotFoundException() {
        when(userRepository.findByEmail(defaultUser.getEmail())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findByEmail(defaultUser.getEmail())).isInstanceOf(
            UserNotFoundException.class);
    }
}