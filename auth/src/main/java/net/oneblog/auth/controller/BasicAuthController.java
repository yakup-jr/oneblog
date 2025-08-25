package net.oneblog.auth.controller;

import lombok.AllArgsConstructor;
import net.oneblog.auth.models.AuthenticationResponseModel;
import net.oneblog.auth.models.BasicRegistrationRequestModel;
import net.oneblog.auth.models.LoginRequestModel;
import net.oneblog.auth.service.BasicAuthService;
import net.oneblog.email.models.RegistrationEmailVerificationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Auth controller.
 */
@RestController
@RequestMapping
@AllArgsConstructor
public class BasicAuthController {

    private final BasicAuthService basicAuthService;

    /**
     * Register response entity.
     *
     * @param basicRegistrationRequestModel the registration request dto
     * @return the response entity
     */
    @PostMapping("/registration")
    public ResponseEntity<String> register(
        @RequestBody BasicRegistrationRequestModel basicRegistrationRequestModel) {
        basicAuthService.register(basicRegistrationRequestModel);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Verify email response entity.
     *
     * @param emailVerification the email verification
     * @return the response entity
     */
    @PostMapping("/registration/email/verify")
    public ResponseEntity<String> verifyEmail(
        @RequestBody RegistrationEmailVerificationModel emailVerification) {
        basicAuthService.verifyEmail(emailVerification);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Login response entity.
     *
     * @param request the request
     * @return the response entity
     */
    @PostMapping("/login/basic")
    public ResponseEntity<AuthenticationResponseModel> login(
        @RequestBody LoginRequestModel request) {
        return ResponseEntity.status(HttpStatus.OK).body(basicAuthService.authenticate(request));
    }
}
