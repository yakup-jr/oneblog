package net.oneblog.auth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import net.oneblog.auth.dto.AuthenticationResponseDto;
import net.oneblog.auth.entity.AuthEntity;
import net.oneblog.auth.repository.AuthRepository;
import net.oneblog.user.entity.UserEntity;
import net.oneblog.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;
import java.util.Random;

@Service
public class GoogleOAuth2LoginService {

    private final UserRepository userRepository;
    private final AuthRepository authRepository;
    private final JwtService jwtService;
    private final TokenService tokenService;
    private final GoogleIdTokenVerifier verifier;


    public GoogleOAuth2LoginService(UserRepository userRepository, AuthRepository authRepository,
                                    JwtService jwtService, TokenService tokenService,
                                    GoogleIdTokenVerifier verifier) {
        this.userRepository = userRepository;
        this.authRepository = authRepository;
        this.jwtService = jwtService;
        this.tokenService = tokenService;
        this.verifier = verifier;
    }


    //todo: throw if nickname exists
    public AuthenticationResponseDto signUp(GoogleIdToken.Payload payload) {
        String nickname = String.valueOf(payload.get("given_name"));
        if (userRepository.findByNickname(nickname).isPresent()) {
            nickname = nickname.concat(String.valueOf(new Random().nextInt(10000)));
        }

        UserEntity user = UserEntity.builder().email(String.valueOf(payload.get("email"))).name(
            String.valueOf(payload.get("name"))).nickname(nickname).build();
        AuthEntity savedAuthEntity = authRepository.save(
            AuthEntity.builder().userEntity(user).googleUserId(payload.getSubject()).build());

        String accessToken = jwtService.generateAccessToken(savedAuthEntity.getUserEntity());
        String refreshToken = jwtService.generateRefreshToken(savedAuthEntity.getUserEntity());

        tokenService.revokeAllTokensForUser(savedAuthEntity.getUserEntity());
        tokenService.saveUserToken(accessToken, refreshToken, savedAuthEntity.getUserEntity());

        return new AuthenticationResponseDto(accessToken, refreshToken);
    }

    public AuthenticationResponseDto login(String token)
        throws GeneralSecurityException, IOException {
        GoogleIdToken.Payload payload = verify(token);

        String email = String.valueOf(payload.get("email"));
        String googleUserId = String.valueOf(payload.getSubject());

        Optional<UserEntity> byGoogleUserId = authRepository.findByGoogleUserId(googleUserId);
        if (byGoogleUserId.isPresent()) {
            UserEntity userEntity = byGoogleUserId.get();

            String accessToken = jwtService.generateAccessToken(userEntity);
            String refreshToken = jwtService.generateRefreshToken(userEntity);

            tokenService.revokeAllTokensForUser(userEntity);
            tokenService.saveUserToken(accessToken, refreshToken, userEntity);

            return new AuthenticationResponseDto(accessToken, refreshToken);
        }
        Optional<AuthEntity> byEmail = authRepository.findByEmail(email);
        if (byEmail.isPresent()) {
            AuthEntity authEntity = byEmail.get();
            authEntity.setGoogleUserId(googleUserId);
            AuthEntity savedUserEntity = authRepository.save(authEntity);

            String accessToken = jwtService.generateAccessToken(savedUserEntity.getUserEntity());
            String refreshToken = jwtService.generateRefreshToken(savedUserEntity.getUserEntity());

            tokenService.revokeAllTokensForUser(savedUserEntity.getUserEntity());
            tokenService.saveUserToken(accessToken, refreshToken, savedUserEntity.getUserEntity());

            return new AuthenticationResponseDto(accessToken, refreshToken);
        }
        return signUp(payload);
    }

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
