package net.oneblog.auth.models;


/**
 * The type Registration request dto.
 */
public record RegistrationRequestModel(String name, String username, String email, String password) {
}
