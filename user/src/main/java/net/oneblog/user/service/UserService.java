package net.oneblog.user.service;

import net.oneblog.api.dto.UserDto;
import net.oneblog.user.dto.UserCreateDto;
import org.springframework.data.domain.Page;

/**
 * The interface User service.
 */
public interface UserService {

    /**
     * Save user.
     *
     * @param user the user
     * @return the user
     */
    UserDto save(UserCreateDto user);

    /**
     * Find all page.
     *
     * @param page the page
     * @param size the size
     * @return the page
     */
    Page<UserDto> findAll(Integer page, Integer size);

    /**
     * Exists by id boolean.
     *
     * @param userId the user id
     * @return the boolean
     */
    boolean existsById(Long userId);

    /**
     * Find by id user.
     *
     * @param id the id
     * @return the user
     */
    UserDto findById(Long id);

    /**
     * Find by nickname user.
     *
     * @param nickname the nickname
     * @return the user
     */
    UserDto findByNickname(String nickname);

    /**
     * Find by email user.
     *
     * @param email the email
     * @return the user
     */
    UserDto findByEmail(String email);

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
