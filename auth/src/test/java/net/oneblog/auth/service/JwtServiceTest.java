package net.oneblog.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import net.oneblog.auth.entity.TokenEntity;
import net.oneblog.auth.repository.TokenRepository;
import net.oneblog.user.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtServiceImpl jwtService;

    @Mock
    private TokenRepository tokenRepository;

    private final String secretKey = "dGVzdC1zZWNyZXQta2V5LWZvci1qd3QtdGVzdGluZy1wdXJwb3Nlcy1vbmx5";
    private final Long accessTokenExpiration = 86400000L;
    private final Long refreshTokenExpiration = 604800000L;
    private UserEntity userEntity;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtService, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtService, "accessTokenExpiration", accessTokenExpiration);
        ReflectionTestUtils.setField(jwtService, "refreshTokenExpiration", refreshTokenExpiration);

        userEntity = UserEntity.builder()
            .userId(1L)
            .nickname("testuser")
            .email("test@example.com")
            .build();

        userDetails = User.builder()
            .username("testuser")
            .password("password")
            .authorities("ROLE_USER")
            .build();
    }

    @Test
    void isValidAccess_Success() {
        String token = jwtService.generateAccessToken(userEntity);
        TokenEntity tokenEntity = TokenEntity.builder()
            .accessToken(token)
            .isRevoke(false)
            .build();

        when(tokenRepository.findByAccessToken(token)).thenReturn(Optional.of(tokenEntity));

        assertTrue(jwtService.isValidAccess(token, userDetails));
    }

    @Test
    void isValidAccess_Invalid() {
        String token = jwtService.generateAccessToken(userEntity);
        TokenEntity tokenEntity = TokenEntity.builder()
            .accessToken(token)
            .isRevoke(true)
            .build();

        when(tokenRepository.findByAccessToken(token)).thenReturn(Optional.of(tokenEntity));

        assertFalse(jwtService.isValidAccess(token, userDetails));
    }

    @Test
    void isValidAccess_Expired() {
        String expiredToken = generateExpiredToken(userEntity.getNickname());
        TokenEntity tokenEntity = TokenEntity.builder()
            .accessToken(expiredToken)
            .isRevoke(false)
            .build();

        assertFalse(jwtService.isValidAccess(expiredToken, userDetails));
    }

    @Test
    void isValidAccess_TokenNotFound() {
        String token = jwtService.generateAccessToken(userEntity);
        when(tokenRepository.findByAccessToken(token)).thenReturn(Optional.empty());

        assertFalse(jwtService.isValidAccess(token, userDetails));
    }

    @Test
    void isValidAccess_UsernameMismatch() {
        String token = jwtService.generateAccessToken(userEntity);
        TokenEntity tokenEntity = TokenEntity.builder()
            .accessToken(token)
            .isRevoke(false)
            .build();

        UserDetails differentUser = User.builder()
            .username("differentuser")
            .password("password")
            .authorities("ROLE_USER")
            .build();

        when(tokenRepository.findByAccessToken(token)).thenReturn(Optional.of(tokenEntity));

        assertFalse(jwtService.isValidAccess(token, differentUser));
    }

    @Test
    void isValidRefresh_Success() {
        String token = jwtService.generateRefreshToken(userEntity);
        TokenEntity tokenEntity = TokenEntity.builder()
            .refreshToken(token)
            .isRevoke(false)
            .build();

        when(tokenRepository.findByRefreshToken(token)).thenReturn(Optional.of(tokenEntity));

        assertTrue(jwtService.isValidRefresh(token, userEntity));
    }

    @Test
    void isValidRefresh_Invalid() {
        String token = jwtService.generateRefreshToken(userEntity);
        TokenEntity tokenEntity = TokenEntity.builder()
            .refreshToken(token)
            .isRevoke(true)
            .build();

        when(tokenRepository.findByRefreshToken(token)).thenReturn(Optional.of(tokenEntity));

        assertFalse(jwtService.isValidRefresh(token, userEntity));
    }

    @Test
    void isValidRefresh_Expired() {
        String expiredToken = generateExpiredToken(userEntity.getNickname());
        TokenEntity tokenEntity = TokenEntity.builder()
            .refreshToken(expiredToken)
            .isRevoke(false)
            .build();

        assertFalse(jwtService.isValidRefresh(expiredToken, userEntity));
    }

    @Test
    void isValidRefresh_TokenNotFound() {
        String token = jwtService.generateRefreshToken(userEntity);
        when(tokenRepository.findByRefreshToken(token)).thenReturn(Optional.empty());

        assertFalse(jwtService.isValidRefresh(token, userEntity));
    }

    @Test
    void isValidRefresh_UsernameMismatch() {
        String token = jwtService.generateRefreshToken(userEntity);
        TokenEntity tokenEntity = TokenEntity.builder()
            .refreshToken(token)
            .isRevoke(false)
            .build();

        UserEntity differentUser = UserEntity.builder()
            .nickname("differentuser")
            .build();

        when(tokenRepository.findByRefreshToken(token)).thenReturn(Optional.of(tokenEntity));

        assertFalse(jwtService.isValidRefresh(token, differentUser));
    }

    @Test
    void extractUsername_Success() {
        String token = jwtService.generateAccessToken(userEntity);

        assertEquals("testuser", jwtService.extractUsername(token));
    }

    @Test
    void extractUsername_InvalidToken() {
        assertThrows(JwtException.class, () -> jwtService.extractUsername("invalid.token.here"));
    }

    @Test
    void extractClaim_Success() {
        String token = jwtService.generateAccessToken(userEntity);
        Function<Claims, String> claimsResolver = Claims::getSubject;

        assertEquals("testuser", jwtService.extractClaim(token, claimsResolver));
    }

    @Test
    void extractClaim_ExpiredToken() {
        String expiredToken = generateExpiredToken(userEntity.getNickname());
        Function<Claims, String> claimsResolver = Claims::getSubject;

        assertThrows(ExpiredJwtException.class,
            () -> jwtService.extractClaim(expiredToken, claimsResolver));
    }

    @Test
    void generateAccessToken_Success() {
        String token = jwtService.generateAccessToken(userEntity);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertEquals("testuser", jwtService.extractUsername(token));
    }

    @Test
    void generateRefreshToken_Success() {
        String token = jwtService.generateRefreshToken(userEntity);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertEquals("testuser", jwtService.extractUsername(token));
    }

    @Test
    void generateTokens_DifferentExpirationTimes() {
        String accessToken = jwtService.generateAccessToken(userEntity);
        String refreshToken = jwtService.generateRefreshToken(userEntity);

        Date accessExpiration = jwtService.extractClaim(accessToken, Claims::getExpiration);
        Date refreshExpiration = jwtService.extractClaim(refreshToken, Claims::getExpiration);

        assertTrue(refreshExpiration.after(accessExpiration));
    }

    private String generateExpiredToken(String username) {
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
        return Jwts.builder()
            .subject(username)
            .issuedAt(new Date(System.currentTimeMillis() - 1000000))
            .expiration(new Date(System.currentTimeMillis() - 1000))
            .signWith(key)
            .compact();
    }
}