package com.oneblog.auth.handler;

import com.oneblog.auth.Token;
import com.oneblog.auth.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

/**
 * The type Custom logout handler.
 */
@Component
public class CustomLogoutHandler implements LogoutHandler {
	
	private final TokenRepository tokenRepository;

	/**
	 * Instantiates a new Custom logout handler.
	 *
	 * @param tokenRepository the token repository
	 */
	public CustomLogoutHandler(TokenRepository tokenRepository) {
		this.tokenRepository = tokenRepository;
	}

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		String authHeader = request.getHeader("Authorization");

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return;
		}

		String token = authHeader.substring(7);

		Token tokenEntity = tokenRepository.findByAccessToken(token).orElse(null);

		if (tokenEntity != null) {
			tokenEntity.setLoggedOut(true);
			tokenRepository.save(tokenEntity);
		}
	}
}
