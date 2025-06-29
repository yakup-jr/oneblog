package com.oneblog.auth.jwt;

import com.oneblog.user.User;
import io.jsonwebtoken.Claims;
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
	 * @param token the token
	 * @param user  the user
	 * @return the boolean
	 */
	boolean isValidRefresh(String token, User user);

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
	 * @param user the user
	 * @return the string
	 */
	String generateAccessToken(User user);

	/**
	 * Generate refresh token string.
	 *
	 * @param user the user
	 * @return the string
	 */
	String generateRefreshToken(User user);

}
