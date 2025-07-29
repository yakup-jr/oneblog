package net.oneblog.email.service;

import net.oneblog.api.dto.UserDto;
import net.oneblog.email.entity.EmailEntity;
import net.oneblog.email.models.RegistrationEmailVerificationModel;
import net.oneblog.email.repository.EmailVerificationRepository;
import net.oneblog.sharedexceptions.ServiceException;
import net.oneblog.user.mappers.UserMapper;
import net.oneblog.user.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * The type Email verification service.
 */
@Service
public class EmailVerificationServiceImpl implements EmailVerificationService {

    private final EmailVerificationRepository emailVerificationRepository;
    private final VerificationMailMessage mailMessage;
    private final UserService userService;
    private final UserMapper userMapper;

    /**
     * Instantiates a new Email verification service.
     *
     * @param emailVerificationRepository the email verification repository
     * @param mailMessage                 the mail message
     * @param userService                 the user service
     * @param userMapper                  the user mapper
     */
    public EmailVerificationServiceImpl(EmailVerificationRepository emailVerificationRepository,
                                        VerificationMailMessage mailMessage,
                                        UserService userService, UserMapper userMapper) {
        this.emailVerificationRepository = emailVerificationRepository;
        this.mailMessage = mailMessage;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @Override
    public void sendVerificationCode(String email) {
        if (emailVerificationRepository.existsByEmailAndCodeNotExpired(email)) {
            throw new ServiceException("Verification code already send");
        }
        String code = mailMessage.sendVerificationCode(email);
        System.out.println("Code is " + code);
        UserDto userEntity = userService.findByEmail(email);
        EmailEntity emailEntity =
            EmailEntity.builder()
                .code(code)
                .userEntity(userMapper.map(userEntity))
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .build();
        emailVerificationRepository.save(emailEntity);
    }

    @Override
    public boolean verifyCode(RegistrationEmailVerificationModel verificationPayload) {
        EmailEntity verification =
            emailVerificationRepository.findByEmail(verificationPayload.email()).orElseThrow(
                () -> new ServiceException("Invalid code"));
        emailVerificationRepository.delete(verification);
        return verification.getCode().equals(verificationPayload.verificationCode()) &&
            verification.getExpiresAt().isAfter(LocalDateTime.now());
    }
}
