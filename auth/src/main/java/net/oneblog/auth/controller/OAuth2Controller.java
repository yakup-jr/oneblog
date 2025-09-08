package net.oneblog.auth.controller;

import lombok.AllArgsConstructor;
import net.oneblog.auth.models.AuthenticationResponseModel;
import net.oneblog.auth.service.GoogleOAuth2LoginService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type O auth 2 controller.
 */
@AllArgsConstructor
@RestController
@RequestMapping("/login/oauth2")
public class OAuth2Controller {

    private final GoogleOAuth2LoginService oauthService;

    /**
     * Authentication oauth response entity.
     *
     * @param token the token
     * @return the response entity
     */
    @PostMapping("/google")
    public ResponseEntity<AuthenticationResponseModel> authenticationOauth(
        @RequestBody String token) {
        AuthenticationResponseModel authenticationResponseModel = oauthService.login(token);
        return ResponseEntity.status(HttpStatus.OK).body(authenticationResponseModel);
    }
}
