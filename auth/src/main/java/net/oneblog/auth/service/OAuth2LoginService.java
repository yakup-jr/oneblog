package net.oneblog.auth.service;

import net.oneblog.auth.dto.AuthenticationResponseDto;

public interface OAuth2LoginService {

    String getAuthorizationUrl();

    AuthenticationResponseDto login(String code);

}
