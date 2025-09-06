package net.oneblog.auth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import lombok.AllArgsConstructor;
import net.oneblog.auth.models.AuthModel;
import net.oneblog.auth.models.AuthenticationResponseModel;
import net.oneblog.auth.models.GoogleRegistrationRequestModel;
import net.oneblog.sharedexceptions.ServiceException;
import net.oneblog.user.service.UserValidationService;
import net.oneblog.validationapi.models.ValidatedUserModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The type Google o auth 2 login service.
 */
@Service
@AllArgsConstructor
public class GoogleOAuth2LoginService {

    private final UserValidationService userValidationService;
    private final AuthService authService;
    private final JwtService jwtService;
    private final TokenManagerService tokenManagerService;
    private final GoogleIdTokenVerifier verifier;
    private final GoogleVerificationService googleVerificationService;

    /**
     * Sign up authentication response dto.
     *
     * @param payload the payload
     * @return the authentication response dto
     */
    @Transactional
    public AuthenticationResponseModel signUp(GoogleIdToken.Payload payload) {
        String nickname = String.valueOf(payload.get("given_name"));
        if (userValidationService.existsByNickname(nickname)) {
            nickname = nickname.concat(String.valueOf(ThreadLocalRandom.current().nextInt(10000)));
        }

        ValidatedUserModel user =
            ValidatedUserModel.builder().email(String.valueOf(payload.get("email"))).name(
                String.valueOf(payload.get("name"))).nickname(nickname).build();
        AuthModel savedAuthEntity = authService.save(
            new GoogleRegistrationRequestModel(String.valueOf(payload.getSubject()), user));

        return generateAndSaveTokens(savedAuthEntity.getUserDto());
    }

    /**
     * Login authentication response dto.
     *
     * @param token the token
     * @return the authentication response dto
     */
    @Transactional
    public AuthenticationResponseModel login(String token) {
        try {
            GoogleIdToken.Payload payload = googleVerificationService.verify(token);
            String googleUserId = String.valueOf(payload.getSubject());

            AuthModel authModel = authService.findByGoogleUserId(googleUserId);

            return generateAndSaveTokens(authModel.getUserDto());
        } catch (GeneralSecurityException | IOException e) {
            throw new ServiceException("Failed to verify Google token");
        }
    }

    private AuthenticationResponseModel generateAndSaveTokens(ValidatedUserModel user) {
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        tokenManagerService.revokeAllTokensForUser(user);
        tokenManagerService.saveUserToken(accessToken, refreshToken, user);

        return new AuthenticationResponseModel(accessToken, refreshToken);
    }
}
