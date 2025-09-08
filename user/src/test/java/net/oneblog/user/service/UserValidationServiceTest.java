package net.oneblog.user.service;

import net.oneblog.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserValidationServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserValidationServiceImpl userValidationService;

    @Test
    void existsById_ReturnTrue() {
        when(userRepository.existsById(1L)).thenReturn(true);

        boolean userExists = userValidationService.existsById(1L);

        assertThat(userExists).isTrue();
    }

    @Test
    void existsById_ReturnFalse() {
        when(userRepository.existsById(999L)).thenReturn(false);

        boolean userExists = userValidationService.existsById(999L);

        assertThat(userExists).isFalse();
    }

    @Test
    void existsByEmail_ReturnTrue() {
        when(userRepository.existsByEmail("email@email.com")).thenReturn(true);

        boolean userExists = userValidationService.existsByEmail("email@email.com");

        assertThat(userExists).isTrue();
    }

    @Test
    void existsByEmail_ReturnFalse() {
        when(userRepository.existsByEmail("wrong")).thenReturn(false);

        boolean userExists = userValidationService.existsByEmail("wrong");

        assertThat(userExists).isFalse();
    }

    @Test
    void existsByNickname_ReturnTrue() {
        when(userRepository.existsByNickname("hunter")).thenReturn(true);

        boolean userExists = userValidationService.existsByNickname("hunter");

        assertThat(userExists).isTrue();
    }

    @Test
    void existsByNickname_ReturnFalse() {
        when(userRepository.existsByEmail(" ")).thenReturn(false);

        boolean userExists = userValidationService.existsByEmail(" ");

        assertThat(userExists).isFalse();
    }

}