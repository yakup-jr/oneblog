package net.oneblog.auth.dto;

/**
 * The type Authentication response dto.
 */
public record AuthenticationResponseDto(String accessToken, String refreshToken) {
}
