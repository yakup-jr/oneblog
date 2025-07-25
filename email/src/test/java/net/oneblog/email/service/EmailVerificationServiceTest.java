package net.oneblog.email.service;

import net.oneblog.email.dto.RegistrationEmailVerification;
import net.oneblog.email.entity.EmailEntity;
import net.oneblog.email.repository.EmailVerificationRepository;
import net.oneblog.sharedexceptions.ServiceException;
import net.oneblog.user.dto.UserDto;
import net.oneblog.user.entity.UserEntity;
import net.oneblog.user.mappers.UserMapper;
import net.oneblog.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailVerificationServiceTest {

    @InjectMocks
    private EmailVerificationServiceImpl emailVerificationService;

    @Mock
    private EmailVerificationRepository emailVerificationRepository;
    @Mock
    private VerificationMailMessage verificationMailMessage;
    @Mock
    private UserService userService;
    @Mock
    private UserMapper userMapper;

    @Test
    void sendVerificationCode_Success() {
        String email = "somemail@mail.com";
        String code = "152734";
        UserDto userDto = UserDto.builder().email(email).build();
        UserEntity userEntity = new UserEntity();

        when(emailVerificationRepository.existsByEmailAndCodeNotExpired(email)).thenReturn(false);
        when(verificationMailMessage.sendVerificationCode(email)).thenReturn(code);
        when(userService.findByEmail(email)).thenReturn(userDto);
        when(userMapper.map(userDto)).thenReturn(userEntity);

        emailVerificationService.sendVerificationCode(email);

        verify(emailVerificationRepository).save(any(EmailEntity.class));
    }

    @Test
    void sendVerificationCode_VerificationCodeAlreadySend() {
        String email = "somemail@mail.com";
        when(emailVerificationRepository.existsByEmailAndCodeNotExpired(email)).thenReturn(true);

        assertThrows(ServiceException.class,
            () -> emailVerificationService.sendVerificationCode(email));
        verify(verificationMailMessage, never()).sendVerificationCode(anyString());
        verify(emailVerificationRepository, never()).save(any(EmailEntity.class));
    }

    @Test
    void verifyCode_Success() {
        String email = "somemail@mail.com";
        String code = "152734";
        RegistrationEmailVerification verification = new RegistrationEmailVerification(email, code);

        EmailEntity emailEntity = EmailEntity.builder()
            .code(code)
            .expiresAt(LocalDateTime.now().plusMinutes(5))
            .build();

        when(emailVerificationRepository.findByEmail(email)).thenReturn(Optional.of(emailEntity));

        boolean result = emailVerificationService.verifyCode(verification);

        assertTrue(result);
        verify(emailVerificationRepository).delete(emailEntity);
    }

    @Test
    void verifyCode_InvalidCode() {
        String email = "somemail@mail.com";
        String code = "152734";
        String wrongCode = "999999";
        RegistrationEmailVerification verification =
            new RegistrationEmailVerification(email, wrongCode);

        EmailEntity emailEntity = EmailEntity.builder()
            .code(code)
            .expiresAt(LocalDateTime.now().plusMinutes(5))
            .build();

        when(emailVerificationRepository.findByEmail(email)).thenReturn(Optional.of(emailEntity));

        boolean result = emailVerificationService.verifyCode(verification);

        assertFalse(result);
        verify(emailVerificationRepository).delete(emailEntity);
    }

    @Test
    void verifyCode_ExpiredCode() {
        String email = "somemail@mail.com";
        String code = "152734";
        RegistrationEmailVerification verification = new RegistrationEmailVerification(email, code);

        EmailEntity emailEntity = EmailEntity.builder()
            .code(code)
            .expiresAt(LocalDateTime.now().minusMinutes(5)) // Expired code
            .build();

        when(emailVerificationRepository.findByEmail(email)).thenReturn(Optional.of(emailEntity));

        boolean result = emailVerificationService.verifyCode(verification);

        assertFalse(result);
        verify(emailVerificationRepository).delete(emailEntity);
    }

    @Test
    void verifyCode_EmailNotFound() {
        String email = "somemail@mail.com";
        String code = "152734";
        RegistrationEmailVerification verification = new RegistrationEmailVerification(email, code);

        when(emailVerificationRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(ServiceException.class,
            () -> emailVerificationService.verifyCode(verification));
        verify(emailVerificationRepository, never()).delete(any());
    }
}
