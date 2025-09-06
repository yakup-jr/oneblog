package net.oneblog.auth.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import net.oneblog.auth.repository.TokenRepository;
import net.oneblog.validationapi.models.ValidatedUserModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.function.Function;

/**
 * The type Jwt service.
 */
@Service
public class JwtServiceImpl implements JwtService {

    @Value("${security.jwt.secret_key}")
    private String secretKey;

    @Value("${security.jwt.access_token_expiration}")
    private Long accessTokenExpiration;

    @Value("${security.jwt.refresh_token_expiration}")
    private Long refreshTokenExpiration;

    private final TokenRepository tokenRepository;

    /**
     * Instantiates a new Jwt service.
     *
     * @param tokenRepository the token repository
     */
    public JwtServiceImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }


    public boolean isValidAccess(String token, UserDetails user) {
        try {
            String username = extractUsername(token);
            boolean isValidToken = tokenRepository.findByAccessToken(token)
                .map(t -> !t.getIsRevoke()).orElse(false);

            return isValidToken
                && isAccessTokenExpired(token)
                && username.equals(user.getUsername());
        } catch (ExpiredJwtException e) {
            return false;
        }
    }


    public boolean isValidRefresh(String token, ValidatedUserModel userEntity) {
        try {
            String username = extractUsername(token);

            boolean isValidRefreshToken = tokenRepository.findByRefreshToken(token)
                .map(t -> !t.getIsRevoke()).orElse(false);

            return username.equals(userEntity.nickname())
                && isRefreshTokenExpired(token)
                && isValidRefreshToken;
        } catch (ExpiredJwtException e) {
            return false;
        }
    }


    private boolean isAccessTokenExpired(String token) {
        return !extractExpiration(token).isBefore(LocalDate.now());
    }

    private boolean isRefreshTokenExpired(String token) {
        return !extractExpiration(token).isBefore(LocalDate.now());
    }

    public LocalDate extractExpiration(String token) {
        return LocalDate.ofInstant(extractClaim(token, Claims::getExpiration).toInstant(),
            ZoneId.systemDefault());
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }


    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }


    private Claims extractAllClaims(String token) {

        JwtParserBuilder parser = Jwts.parser();

        parser.verifyWith(getSigningKey());

        return parser.build()
            .parseSignedClaims(token)
            .getPayload();
    }


    public String generateAccessToken(ValidatedUserModel userEntity) {
        return generateToken(userEntity, accessTokenExpiration);
    }


    public String generateRefreshToken(ValidatedUserModel userEntity) {
        return generateToken(userEntity, refreshTokenExpiration);
    }


    private String generateToken(ValidatedUserModel userEntity, long expiryTime) {
        LocalDateTime ldt = LocalDateTime.now();
        ZonedDateTime zdt = ldt.atZone(ZoneId.systemDefault());

        Date issued = Date.from(zdt.toInstant());
        Date expiration = Date.from(zdt.plusSeconds(expiryTime).toInstant());

        JwtBuilder builder = Jwts.builder()
            .subject(userEntity.nickname())
            .issuedAt(issued)
            .expiration(expiration)
            .signWith(getSigningKey());

        return builder.compact();
    }


    private SecretKey getSigningKey() {

        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}
