package com.oneblog.user;

import com.oneblog.article.ArticleLink;
import com.oneblog.article.ArticleNotFoundException;
import com.oneblog.exceptions.ApiRequestException;
import com.oneblog.user.dto.UserCreateDto;
import com.oneblog.user.dto.UserDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

	private final UserMapper userMapper;

	private final UserService userService;

	private final UserLink userLink;

	private final ArticleLink articleLink;

	public UserController(UserMapper userMapper, UserService userService, UserLink userLink, ArticleLink articleLink) {
		this.userMapper = userMapper;
		this.userService = userService;
		this.userLink = userLink;
		this.articleLink = articleLink;
	}

	@PostMapping("/")
	public ResponseEntity<EntityModel<UserDto>> saveUser(@RequestBody UserCreateDto userCreateDto) {
		try {
			User mappedUser = userMapper.map(userCreateDto);
			User savedUser = userService.save(mappedUser);
			List<Link> links = List.of(userLink.findUserByUserId(savedUser.getUserId()).withRel("user"));
			return ResponseEntity.status(HttpStatus.CREATED).body(EntityModel.of(userMapper.map(savedUser), links));
		} catch (ApiRequestException e) {
			throw new ApiRequestException(e.getMessage());
		}
	}

	@GetMapping("/{userId}")
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

	@GetMapping("/nickname/{nickname}")
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

	@GetMapping("/article/{articleId}")
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

	@DeleteMapping("/{userId}")
	public ResponseEntity<EntityModel<UserDto>> deleteUser(@PathVariable Long userId) {
		try {
			userService.deleteById(userId);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (UserNotFoundException e) {
			throw new UserNotFoundException(e.getMessage());
		}
	}

}
