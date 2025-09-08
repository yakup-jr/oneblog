package net.oneblog.auth.service;

import io.jsonwebtoken.io.SerialException;
import net.oneblog.auth.entity.AuthEntity;
import net.oneblog.auth.entity.TokenEntity;
import net.oneblog.auth.repository.AuthRepository;
import net.oneblog.auth.repository.TokenRepository;
import net.oneblog.validationapi.models.ValidatedUserModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenManagerServiceTest {

    @InjectMocks
    private TokenManagerServiceImpl tokenManagerService;
    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthRepository authRepository;

    @Test
    void revokeAllTokensForUser_Success() {
        ValidatedUserModel userModel = ValidatedUserModel.builder()
            .userId(1L)
            .build();

        List<TokenEntity> tokens = List.of(
            TokenEntity.builder().isRevoke(false).build(),
            TokenEntity.builder().isRevoke(false).build()
        );

        when(tokenRepository.findAllAccessTokenByUser(userModel.userId())).thenReturn(tokens);

        tokenManagerService.revokeAllTokensForUser(userModel);

        tokens.forEach(token -> assertTrue(token.getIsRevoke()));
        verify(tokenRepository).saveAll(tokens);
    }

    @Test
    void saveUserToken_Success() {
        ValidatedUserModel userModel = ValidatedUserModel.builder()
            .userId(1L)
            .email("test@example.com")
            .build();

        AuthEntity authEntity = AuthEntity.builder()
            .tokens(new ArrayList<>())
            .build();

        when(jwtService.extractExpiration("access-token")).thenReturn(LocalDate.now().plusDays(1));
        when(authRepository.findByEmail(userModel.email())).thenReturn(Optional.of(authEntity));

        tokenManagerService.saveUserToken("access-token", "refresh-token", userModel);

        verify(tokenRepository).save(any(TokenEntity.class));
        assertEquals(1, authEntity.getTokens().size());
    }

    @Test
    void saveUserToken_UserNotFound() {
        ValidatedUserModel userModel = ValidatedUserModel.builder()
            .email("test@example.com")
            .build();

        when(authRepository.findByEmail(userModel.email())).thenReturn(Optional.empty());

        assertThrows(SerialException.class,
            () -> tokenManagerService.saveUserToken("access-token", "refresh-token", userModel));
    }

}