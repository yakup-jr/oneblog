package net.oneblog.email.service;

import lombok.AllArgsConstructor;
import net.oneblog.email.entity.EmailEntity;
import net.oneblog.email.models.RegistrationEmailVerificationModel;
import net.oneblog.email.repository.EmailVerificationRepository;
import net.oneblog.sharedexceptions.ServiceException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * The type Email verification service.
 */
@Service
@AllArgsConstructor
public class EmailVerificationServiceImpl implements EmailVerificationService {

    private final EmailVerificationRepository emailVerificationRepository;
    private final VerificationMailMessage mailMessage;

    @Override
    public void sendVerificationCode(String email) {
        if (emailVerificationRepository.existsByEmailAndCodeNotExpired(email)) {
            throw new ServiceException("Verification code already send");
        }
        String code = mailMessage.sendVerificationCode(email);
        EmailEntity emailEntity = EmailEntity.builder()
                .code(code)
                .email(email)
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .build();
        emailVerificationRepository.save(emailEntity);
        System.out.println("Saved email entity: " + emailEntity.getEmailId());
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
