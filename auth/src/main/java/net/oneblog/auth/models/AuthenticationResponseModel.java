package net.oneblog.auth.models;

/**
 * The type Authentication response dto.
 */
public record AuthenticationResponseModel(String accessToken, String refreshToken) {
}
