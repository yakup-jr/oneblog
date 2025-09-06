package net.oneblog.auth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import lombok.AllArgsConstructor;
import net.oneblog.auth.models.AuthModel;
import net.oneblog.auth.models.AuthenticationResponseModel;
import net.oneblog.auth.models.GoogleRegistrationRequestModel;
import net.oneblog.sharedexceptions.ServiceException;
import net.oneblog.user.service.UserService;
import net.oneblog.user.service.UserValidationService;
import net.oneblog.validationapi.models.ValidatedUserModel;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The type Google o auth 2 login service.
 */
@Service
@AllArgsConstructor
public class GoogleOAuth2LoginService {

    private final UserService userService;
    private final UserValidationService userValidationService;
    private final AuthService authService;
    private final JwtService jwtService;
    private final TokenService tokenService;
    private final GoogleIdTokenVerifier verifier;

    /**
     * Sign up authentication response dto.
     *
     * @param payload the payload
     * @return the authentication response dto
     */
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

        String accessToken = jwtService.generateAccessToken(savedAuthEntity.getUserDto());
        String refreshToken = jwtService.generateRefreshToken(savedAuthEntity.getUserDto());

        tokenService.revokeAllTokensForUser(savedAuthEntity.getUserDto());
        tokenService.saveUserToken(accessToken, refreshToken, savedAuthEntity.getUserDto());

        return new AuthenticationResponseModel(accessToken, refreshToken);
    }

    /**
     * Login authentication response dto.
     *
     * @param token the token
     * @return the authentication response dto
     */
    public AuthenticationResponseModel login(String token) {
        GoogleIdToken.Payload payload = verifyByGoogle(token);
        String googleUserId = String.valueOf(payload.getSubject());

        AuthModel authModel = authService.findByGoogleUserId(googleUserId);

        ValidatedUserModel userEntity = authModel.getUserDto();

        String accessToken = jwtService.generateAccessToken(userEntity);
        String refreshToken = jwtService.generateRefreshToken(userEntity);

        tokenService.revokeAllTokensForUser(userEntity);
        tokenService.saveUserToken(accessToken, refreshToken, userEntity);

        return new AuthenticationResponseModel(accessToken, refreshToken);

    }

    private GoogleIdToken.Payload verifyByGoogle(String token) {
        try {
            return verify(token);
        } catch (GeneralSecurityException | IOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * Verify google id token . payload.
     *
     * @param token the token
     * @return the google id token . payload
     * @throws GeneralSecurityException the general security exception
     * @throws IOException              the io exception
     */
    public GoogleIdToken.Payload verify(String token) throws GeneralSecurityException,
        IOException {
        GoogleIdToken idToken = verifier.verify(getTokenToVerify(token));
        if (idToken == null) {
            throw new GeneralSecurityException("token verification failed");
        }
        if (idToken.getPayload() == null || !idToken.getPayload().getEmailVerified()) {
            throw new GeneralSecurityException("email verification failed");
        }
        return idToken.getPayload();
    }


    private String getTokenToVerify(String token) throws GeneralSecurityException {
        try {
            return token.split("\"")[3];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new GeneralSecurityException("token not valid");
        }
    }
}
