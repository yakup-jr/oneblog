package net.oneblog.auth.service;

import lombok.AllArgsConstructor;
import net.oneblog.auth.models.AuthModel;
import net.oneblog.auth.models.AuthenticationResponseModel;
import net.oneblog.auth.models.BasicRegistrationRequestModel;
import net.oneblog.auth.models.LoginRequestModel;
import net.oneblog.email.exceptions.InvalidVerificationCodeException;
import net.oneblog.email.models.RegistrationEmailVerificationModel;
import net.oneblog.email.service.EmailVerificationService;
import net.oneblog.user.service.UserService;
import net.oneblog.validationapi.models.ValidatedUserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type Auth service.
 */
@Service
@AllArgsConstructor
public class BasicAuthServiceImpl implements BasicAuthService {

    private static final Logger log = LoggerFactory.getLogger(BasicAuthServiceImpl.class);
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final EmailVerificationService emailVerificationService;
    private final AuthService authService;
    private final TokenService tokenService;


    @Override
    public void register(BasicRegistrationRequestModel request) {
        authService.save(request);
        emailVerificationService.sendVerificationCode(request.email());
    }

    @Override
    @Transactional
    public void verifyEmail(RegistrationEmailVerificationModel request) {
        boolean verified = emailVerificationService.verifyCode(request);
        if (!verified) {
            throw new InvalidVerificationCodeException("Code is invalid");
        }
        AuthModel model = authService.findByEmail(request.email());
        model.setVerificated(true);
        authService.update(model);
    }


    @Override
    public AuthenticationResponseModel authenticate(LoginRequestModel request) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
            new UsernamePasswordAuthenticationToken(request.username(), request.password());
        authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        ValidatedUserModel userModel = userService.findByNickname(request.username());


        String accessToken = jwtService.generateAccessToken(userModel);
        String refreshToken = jwtService.generateRefreshToken(userModel);

        tokenService.revokeAllTokensForUser(userModel);

        tokenService.saveUserToken(accessToken, refreshToken, userModel);

        return new AuthenticationResponseModel(accessToken, refreshToken);
    }
}
