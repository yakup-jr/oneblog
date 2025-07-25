package net.oneblog.auth.service;

import net.oneblog.auth.dto.AuthenticationResponseDto;
import net.oneblog.auth.dto.LoginRequestDto;
import net.oneblog.auth.dto.RegistrationRequestDto;
import net.oneblog.email.dto.RegistrationEmailVerification;

public interface BasicAuthService {

    void register(RegistrationRequestDto request);

    void verifyEmail(RegistrationEmailVerification request);

    AuthenticationResponseDto authenticate(LoginRequestDto request);
}
