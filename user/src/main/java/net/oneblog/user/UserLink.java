package net.oneblog.user;

import net.oneblog.user.controller.UserController;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * The type User link.
 */
@Component
public class UserLink {
    private static final Class<UserController> controllerClass = UserController.class;

    /**
     * Find user by user id web mvc link builder.
     *
     * @param userId the user id
     * @return the web mvc link builder
     */
    public WebMvcLinkBuilder findUserByUserId(Long userId) {
        return linkTo(methodOn(controllerClass).findUserByUserId(userId));
    }

    /**
     * Find user by nickname web mvc link builder.
     *
     * @param nickname the nickname
     * @return the web mvc link builder
     */
    public WebMvcLinkBuilder findUserByNickname(String nickname) {
        return linkTo(methodOn(controllerClass).findUserByNickname(nickname));
    }
}
