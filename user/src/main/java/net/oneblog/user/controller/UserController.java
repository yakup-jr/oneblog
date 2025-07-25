package net.oneblog.user.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import net.oneblog.sharedexceptions.PageNotFoundException;
import net.oneblog.user.UserLink;
import net.oneblog.user.UserModelAssembler;
import net.oneblog.user.dto.UserCreateDto;
import net.oneblog.user.dto.UserDto;
import net.oneblog.user.exceptions.UserNotFoundException;
import net.oneblog.user.service.UserService;
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
public class UserController {

    private final UserService userService;
    private final UserLink userLink;
    private final UserModelAssembler userModelAssembler;
    private final PagedResourcesAssembler<UserDto> pagedResourcesAssembler;

    /**
     * Instantiates a new User controller.
     *
     * @param userService             the user service
     * @param userLink                the user link
     * @param userModelAssembler      the user model assembler
     * @param pagedResourcesAssembler the paged resources assembler
     */
    public UserController(
        UserService userService, UserLink userLink,
        UserModelAssembler userModelAssembler,
        PagedResourcesAssembler<UserDto> pagedResourcesAssembler) {
        this.userService = userService;
        this.userLink = userLink;
        this.userModelAssembler = userModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    /**
     * Save user response entity.
     *
     * @param userCreateDto the user create dto
     * @return the response entity
     */
    @PostMapping("/user")
    public ResponseEntity<EntityModel<UserDto>> saveUser(
        @RequestBody @Validated UserCreateDto userCreateDto) {
        UserDto savedUser = userService.save(userCreateDto);
        List<Link> links =
            List.of(userLink.findUserByUserId(savedUser.getUserId()).withRel("user"));
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
    public ResponseEntity<PagedModel<EntityModel<UserDto>>> findAllUsers(
        @RequestParam @Validated @Min(0) Integer page,
        @RequestParam(required = false, defaultValue = "10") @Validated @Min(1) @Max(50)
        Integer size) {
        try {
            Page<UserDto> userPage = userService.findAll(page, size);
            return ResponseEntity.status(HttpStatus.OK)
                .body(pagedResourcesAssembler.toModel(userPage, userModelAssembler));
        } catch (PageNotFoundException e) {
            throw new PageNotFoundException(e.getMessage());
        }
    }

    /**
     * Find user by user id response entity.
     *
     * @param userId the user id
     * @return the response entity
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<EntityModel<UserDto>> findUserByUserId(@PathVariable Long userId) {
        try {
            UserDto foundUser = userService.findById(userId);
            List<Link> links = List.of(userLink.findUserByUserId(userId).withSelfRel(),
                userLink.findUserByNickname(foundUser.getNickname()).withRel("user"));
            return ResponseEntity.status(HttpStatus.OK)
                .body(EntityModel.of(foundUser, links));
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException(e.getMessage());
        }
    }

    /**
     * Find user by nickname response entity.
     *
     * @param nickname the nickname
     * @return the response entity
     */
    @GetMapping("/user/nickname/{nickname}")
    public ResponseEntity<EntityModel<UserDto>> findUserByNickname(@PathVariable String nickname) {
        try {
            UserDto foundUser = userService.findByNickname(nickname);
            List<Link> links = List.of(userLink.findUserByNickname(nickname).withSelfRel(),
                userLink.findUserByUserId(foundUser.getUserId()).withRel("user"));
            return ResponseEntity.status(HttpStatus.OK)
                .body(EntityModel.of(foundUser, links));
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException(e.getMessage());
        }
    }
}
