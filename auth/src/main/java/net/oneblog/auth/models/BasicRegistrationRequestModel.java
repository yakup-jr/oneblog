package net.oneblog.auth.models;


/**
 * The type Registration request dto.
 */
public record BasicRegistrationRequestModel(String name, String username, String email, String password) {
}
