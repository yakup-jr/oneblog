package net.oneblog.auth.controller;

import net.oneblog.auth.dto.AuthenticationResponseDto;
import net.oneblog.auth.service.GoogleOAuth2LoginService;
import net.oneblog.sharedexceptions.ApiRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * The type O auth 2 controller.
 */
@RestController
@RequestMapping("/login/oauth2")
public class OAuth2Controller {

    private final GoogleOAuth2LoginService oauthService;

    /**
     * Instantiates a new O auth 2 controller.
     *
     * @param oauthService the oauth service
     */
    public OAuth2Controller(GoogleOAuth2LoginService oauthService) {
        this.oauthService = oauthService;
    }

    /**
     * Authentication oauth response entity.
     *
     * @param token the token
     * @return the response entity
     * @throws GeneralSecurityException the general security exception
     */
    @PostMapping("/google")
    public ResponseEntity<AuthenticationResponseDto> authenticationOauth(@RequestBody String token)
        throws GeneralSecurityException {
        try {
            AuthenticationResponseDto authenticationResponseDto = oauthService.login(token);
            return ResponseEntity.status(HttpStatus.OK).body(authenticationResponseDto);
        } catch (GeneralSecurityException e) {
            throw new java.security.SignatureException(e);
        } catch (IOException e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

}
