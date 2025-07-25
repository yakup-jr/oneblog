package net.oneblog.auth.service;

import io.jsonwebtoken.Claims;
import net.oneblog.user.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.function.Function;

/**
 * The interface Jwt service.
 */
public interface JwtService {

    /**
     * Is valid access boolean.
     *
     * @param token the token
     * @param user  the user
     * @return the boolean
     */
    boolean isValidAccess(String token, UserDetails user);

    /**
     * Is valid refresh boolean.
     *
     * @param token      the token
     * @param userEntity the user
     * @return the boolean
     */
    boolean isValidRefresh(String token, UserEntity userEntity);

    /**
     * Extract username string.
     *
     * @param token the token
     * @return the string
     */
    String extractUsername(String token);

    /**
     * Extract claim t.
     *
     * @param <T>      the type parameter
     * @param token    the token
     * @param resolver the resolver
     * @return the t
     */
    <T> T extractClaim(String token, Function<Claims, T> resolver);

    /**
     * Generate access token string.
     *
     * @param userEntity the user
     * @return the string
     */
    String generateAccessToken(UserEntity userEntity);

    /**
     * Generate refresh token string.
     *
     * @param userEntity the user
     * @return the string
     */
    String generateRefreshToken(UserEntity userEntity);

}
