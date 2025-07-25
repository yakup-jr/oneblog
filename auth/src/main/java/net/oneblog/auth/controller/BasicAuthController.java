package net.oneblog.auth.controller;

import net.oneblog.auth.dto.AuthenticationResponseDto;
import net.oneblog.auth.dto.LoginRequestDto;
import net.oneblog.auth.dto.RegistrationRequestDto;
import net.oneblog.auth.service.BasicAuthService;
import net.oneblog.email.dto.RegistrationEmailVerification;
import net.oneblog.sharedexceptions.ApiRequestException;
import net.oneblog.user.service.UserService;
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
public class BasicAuthController {

    private final BasicAuthService basicAuthService;

    private final UserService userService;

    /**
     * Instantiates a new Auth controller.
     *
     * @param basicAuthService the auth service
     * @param userService      the user service
     */
    public BasicAuthController(BasicAuthService basicAuthService, UserService userService) {
        this.basicAuthService = basicAuthService;
        this.userService = userService;
    }

    /**
     * Register response entity.
     *
     * @param registrationRequestDto the registration request dto
     * @return the response entity
     */
    @PostMapping("/registration")
    public ResponseEntity<String> register(
        @RequestBody RegistrationRequestDto registrationRequestDto) {
        if (userService.existsByNickname(registrationRequestDto.getUsername())) {
            throw new ApiRequestException("Username is already taken");
        }

        if (userService.existsByEmail(registrationRequestDto.getEmail())) {
            throw new ApiRequestException("Email is already taken");
        }

        basicAuthService.register(registrationRequestDto);
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
        @RequestBody RegistrationEmailVerification emailVerification) {
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
    public ResponseEntity<AuthenticationResponseDto> login(@RequestBody LoginRequestDto request) {
        return ResponseEntity.status(HttpStatus.OK).body(basicAuthService.authenticate(request));
    }
}
