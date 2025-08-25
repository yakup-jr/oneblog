package net.oneblog.auth.service;

import net.oneblog.auth.models.AuthenticationResponseModel;
import net.oneblog.auth.models.BasicRegistrationRequestModel;
import net.oneblog.auth.models.LoginRequestModel;
import net.oneblog.email.models.RegistrationEmailVerificationModel;

/**
 * The interface Basic auth service.
 */
public interface BasicAuthService {

    /**
     * Register.
     *
     * @param request the request
     */
    void register(BasicRegistrationRequestModel request);

    /**
     * Verify email.
     *
     * @param request the request
     */
    void verifyEmail(RegistrationEmailVerificationModel request);

    /**
     * Authenticate authentication response dto.
     *
     * @param request the request
     * @return the authentication response dto
     */
    AuthenticationResponseModel authenticate(LoginRequestModel request);
}
