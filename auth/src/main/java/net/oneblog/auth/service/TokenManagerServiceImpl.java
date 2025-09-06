package net.oneblog.auth.service;

import io.jsonwebtoken.io.SerialException;
import lombok.AllArgsConstructor;
import net.oneblog.auth.entity.AuthEntity;
import net.oneblog.auth.entity.TokenEntity;
import net.oneblog.auth.repository.AuthRepository;
import net.oneblog.auth.repository.TokenRepository;
import net.oneblog.validationapi.models.ValidatedUserModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class TokenManagerServiceImpl implements TokenManagerService {

    private final TokenRepository tokenRepository;
    private final AuthRepository authRepository;
    private final JwtService jwtService;

    @Override
    @Transactional
    public void revokeAllTokensForUser(ValidatedUserModel user) {
        List<TokenEntity> validToken = tokenRepository.findAllAccessTokenByUser(user.userId());

        if (!validToken.isEmpty()) {
            validToken.forEach(token -> token.setIsRevoke(true));
        }

        tokenRepository.saveAll(validToken);
    }

    @Override
    @Transactional
    public void saveUserToken(String accessToken, String refreshToken, ValidatedUserModel user) {
        AuthEntity authEntity =
            authRepository.findByEmail(user.email()).orElseThrow(() -> new SerialException(
                "user not found"));

        TokenEntity tokenEntity =
            TokenEntity.builder()
                .authEntity(authEntity)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .isRevoke(false)
                .expireAt(jwtService.extractExpiration(accessToken).atStartOfDay())
                .build();
        List<TokenEntity> tokens = authEntity.getTokens();
        tokens.add(tokenEntity);

        tokenRepository.save(tokenEntity);
    }
}
