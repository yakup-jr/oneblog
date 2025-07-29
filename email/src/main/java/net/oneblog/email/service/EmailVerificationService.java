package net.oneblog.email.service;

import net.oneblog.email.models.RegistrationEmailVerificationModel;

/**
 * The interface Email verification service.
 */
public interface EmailVerificationService {

    /**
     * Send verification code.
     *
     * @param email the email
     */
    void sendVerificationCode(String email);

    /**
     * Verify code boolean.
     *
     * @param verification the verification
     * @return the boolean
     */
    boolean verifyCode(RegistrationEmailVerificationModel verification);

}
