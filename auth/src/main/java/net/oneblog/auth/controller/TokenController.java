package net.oneblog.auth.controller;

import net.oneblog.auth.dto.AuthenticationResponseDto;
import net.oneblog.auth.dto.RefreshTokenRequestDto;
import net.oneblog.auth.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Token controller.
 */
@RestController
public class TokenController {

    private final TokenService tokenService;

    /**
     * Instantiates a new Token controller.
     *
     * @param tokenService the token service
     */
    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    /**
     * Refresh token response entity.
     *
     * @param refreshTokenDto the refresh token dto
     * @return the response entity
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponseDto> refreshToken(
        RefreshTokenRequestDto refreshTokenDto) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(tokenService.reIssueRefreshToken(refreshTokenDto));
    }

}
