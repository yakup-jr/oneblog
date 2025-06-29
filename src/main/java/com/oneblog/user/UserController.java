package com.oneblog.user;

import com.oneblog.article.ArticleLink;
import com.oneblog.article.ArticleNotFoundException;
import com.oneblog.exceptions.ApiRequestException;
import com.oneblog.exceptions.PageNotFoundException;
import com.oneblog.user.dto.UserCreateDto;
import com.oneblog.user.dto.UserDto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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

	private final UserMapper userMapper;
	private final UserService userService;
	private final UserLink userLink;
	private final ArticleLink articleLink;
	private final UserModelAssembler userModelAssembler;
	private final PagedResourcesAssembler<UserDto> pagedResourcesAssembler;

	/**
	 * Instantiates a new User controller.
	 *
	 * @param userMapper              the user mapper
	 * @param userService             the user service
	 * @param userLink                the user link
	 * @param articleLink             the article link
	 * @param userModelAssembler      the user model assembler
	 * @param pagedResourcesAssembler the paged resources assembler
	 */
	public UserController(
		UserMapper userMapper, UserService userService, UserLink userLink, ArticleLink articleLink,
		UserModelAssembler userModelAssembler, PagedResourcesAssembler<UserDto> pagedResourcesAssembler) {
		this.userMapper = userMapper;
		this.userService = userService;
		this.userLink = userLink;
		this.articleLink = articleLink;
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
	public ResponseEntity<EntityModel<UserDto>> saveUser(@RequestBody @Validated UserCreateDto userCreateDto) {
		try {
			User mappedUser = userMapper.map(userCreateDto);
			User savedUser = userService.save(mappedUser);
			List<Link> links = List.of(userLink.findUserByUserId(savedUser.getUserId()).withRel("user"));
			return ResponseEntity.status(HttpStatus.CREATED).body(EntityModel.of(userMapper.map(savedUser), links));
		} catch (ApiRequestException e) {
			throw new ApiRequestException(e.getMessage());
		}
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
		@RequestParam(required = false, defaultValue = "10") @Validated @Min(1) @Max(50) Integer size) {
		try {
			Page<UserDto> userPage = userService.findAll(page, size).map(userMapper::map);
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
			User foundUser = userService.findById(userId);
			List<Link> links = List.of(userLink.findUserByUserId(userId).withSelfRel(),
			                           userLink.findUserByNickname(foundUser.getNickname()).withRel("user"),
			                           articleLink.findArticleByUserId(userId).withRel("articles"));
			return ResponseEntity.status(HttpStatus.OK).body(EntityModel.of(userMapper.map(foundUser), links));
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
			User foundUser = userService.findByNickname(nickname);
			List<Link> links = List.of(userLink.findUserByNickname(nickname).withSelfRel(),
			                           userLink.findUserByUserId(foundUser.getUserId()).withRel("user"),
			                           articleLink.findArticleByUserId(foundUser.getUserId()).withRel("articles"));
			return ResponseEntity.status(HttpStatus.OK).body(EntityModel.of(userMapper.map(foundUser), links));
		} catch (UserNotFoundException e) {
			throw new UserNotFoundException(e.getMessage());
		}
	}

	/**
	 * Find user by article id response entity.
	 *
	 * @param articleId the article id
	 * @return the response entity
	 */
	@GetMapping("/user/article/{articleId}")
	public ResponseEntity<EntityModel<UserDto>> findUserByArticleId(@PathVariable Long articleId) {
		try {
			User foundUser = userService.findByArticleId(articleId);
			List<Link> links = List.of(userLink.findUserByArticleId(articleId).withSelfRel(),
			                           userLink.findUserByUserId(foundUser.getUserId()).withRel("user"),
			                           articleLink.findArticleByUserId(foundUser.getUserId()).withRel("articles"));
			return ResponseEntity.status(HttpStatus.OK).body(EntityModel.of(userMapper.map(foundUser), links));
		} catch (UserNotFoundException | ArticleNotFoundException e) {
			throw new UserNotFoundException(e.getMessage());
		}
	}

	/**
	 * Delete user response entity.
	 *
	 * @param userId the user id
	 * @return the response entity
	 */
	@DeleteMapping("/user/{userId}")
	public ResponseEntity<EntityModel<UserDto>> deleteUser(@PathVariable Long userId) {
		try {
			userService.deleteById(userId);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (UserNotFoundException e) {
			throw new UserNotFoundException(e.getMessage());
		}
	}

}
