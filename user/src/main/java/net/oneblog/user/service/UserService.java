package net.oneblog.user.service;

import net.oneblog.user.models.UserCreateRequest;
import net.oneblog.validationapi.models.ValidatedUserModel;
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
    ValidatedUserModel save(UserCreateRequest user);

    /**
     * Find all page.
     *
     * @param page the page
     * @param size the size
     * @return the page
     */
    Page<ValidatedUserModel> findAll(Integer page, Integer size);

    /**
     * Find by id user.
     *
     * @param id the id
     * @return the user
     */
    ValidatedUserModel findById(Long id);

    /**
     * Find by nickname user.
     *
     * @param nickname the nickname
     * @return the user
     */
    ValidatedUserModel findByNickname(String nickname);

    /**
     * Find by email user.
     *
     * @param email the email
     * @return the user
     */
    ValidatedUserModel findByEmail(String email);

}
