package net.oneblog.user.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import net.oneblog.user.links.UserLink;
import net.oneblog.user.models.UserCreateRequest;
import net.oneblog.user.service.UserService;
import net.oneblog.validationapi.mappers.ValidatedUserModelMapper;
import net.oneblog.validationapi.models.ValidatedUserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The type User controller.
 */
@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserLink userLink;
    private final PagedResourcesAssembler<ValidatedUserModel> pagedResourcesAssembler;
    private final ValidatedUserModelMapper modelMapper;

    /**
     * Save user response entity.
     *
     * @param userCreateRequest the user create dto
     * @return the response entity
     */
    @PostMapping("/user")
    public ResponseEntity<EntityModel<ValidatedUserModel>> saveUser(
        @RequestBody @Validated UserCreateRequest userCreateRequest) {
        ValidatedUserModel savedUser = userService.save(userCreateRequest);
        List<Link> links =
            List.of(userLink.findUserByUserId(savedUser.userId()).withRel("user"));
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(EntityModel.of(savedUser, links));
    }

    /**
     * Find all users response entity.
     *
     * @param page the page
     * @param size the size
     * @return the response entity
     */
    @GetMapping("/users")
    public ResponseEntity<PagedModel<EntityModel<ValidatedUserModel>>> findAllUsers(
        @RequestParam @Validated @Min(0) Integer page,
        @RequestParam(required = false, defaultValue = "10") @Validated @Min(1) @Max(50)
        Integer size) {
        Page<ValidatedUserModel> userPage = userService.findAll(page, size);
        return ResponseEntity.status(HttpStatus.OK)
            .body(pagedResourcesAssembler.toModel(userPage));
    }

    /**
     * Find user by user id response entity.
     *
     * @param userId the user id
     * @return the response entity
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<EntityModel<ValidatedUserModel>> findUserByUserId(
        @PathVariable Long userId) {
        ValidatedUserModel foundUser = userService.findById(userId);
        List<Link> links = List.of(userLink.findUserByUserId(userId).withSelfRel(),
            userLink.findUserByNickname(foundUser.nickname()).withRel("user"));
        return ResponseEntity.status(HttpStatus.OK)
            .body(EntityModel.of(foundUser, links));
    }

    /**
     * Find user by nickname response entity.
     *
     * @param nickname the nickname
     * @return the response entity
     */
    @GetMapping("/user/nickname/{nickname}")
    public ResponseEntity<EntityModel<ValidatedUserModel>> findUserByNickname(
        @PathVariable String nickname) {
        ValidatedUserModel foundUser = userService.findByNickname(nickname);
        List<Link> links = List.of(userLink.findUserByNickname(nickname).withSelfRel(),
            userLink.findUserByUserId(foundUser.userId()).withRel("user"));
        return ResponseEntity.status(HttpStatus.OK)
            .body(EntityModel.of(foundUser, links));
    }
}
