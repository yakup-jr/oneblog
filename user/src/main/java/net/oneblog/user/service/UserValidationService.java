package net.oneblog.user.service;

public interface UserValidationService {
    /**
     * Exists by id boolean.
     *
     * @param userId the user id
     * @return the boolean
     */
    boolean existsById(Long userId);

    /**
     * Exists by nickname boolean.
     *
     * @param nickname the nickname
     * @return the boolean
     */
    boolean existsByNickname(String nickname);

    /**
     * Exists by email boolean.
     *
     * @param email the email
     * @return the boolean
     */
    boolean existsByEmail(String email);
}
