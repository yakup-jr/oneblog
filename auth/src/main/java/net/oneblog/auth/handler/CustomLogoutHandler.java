package net.oneblog.auth.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.oneblog.auth.entity.TokenEntity;
import net.oneblog.auth.repository.TokenRepository;
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
    public void logout(HttpServletRequest request, HttpServletResponse response,
                       Authentication authentication) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        String token = authHeader.substring(7);

        TokenEntity tokenEntity = tokenRepository.findByAccessToken(token).orElse(null);

        if (tokenEntity != null) {
            tokenEntity.setIsRevoke(true);
            tokenRepository.save(tokenEntity);
        }
    }
}
