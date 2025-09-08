package net.oneblog.email.models;


/**
 * The type Registration email verification.
 */
public record RegistrationEmailVerificationModel(String email, String verificationCode) {
}
