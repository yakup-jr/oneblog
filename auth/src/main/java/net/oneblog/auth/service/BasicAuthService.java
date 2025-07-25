package net.oneblog.auth.service;

import net.oneblog.auth.dto.AuthenticationResponseDto;
import net.oneblog.auth.dto.LoginRequestDto;
import net.oneblog.auth.dto.RegistrationRequestDto;
import net.oneblog.email.dto.RegistrationEmailVerification;

/**
 * The interface Basic auth service.
 */
public interface BasicAuthService {

    /**
     * Register.
     *
     * @param request the request
     */
    void register(RegistrationRequestDto request);

    /**
     * Verify email.
     *
     * @param request the request
     */
    void verifyEmail(RegistrationEmailVerification request);

    /**
     * Authenticate authentication response dto.
     *
     * @param request the request
     * @return the authentication response dto
     */
    AuthenticationResponseDto authenticate(LoginRequestDto request);
}
