package com.oneblog.user;

import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserLink {
	private static final Class<UserController> controllerClass = UserController.class;

	public WebMvcLinkBuilder findUserByUserId(Long userId) {
		return linkTo(methodOn(controllerClass).findUserByUserId(userId));
	}

	public WebMvcLinkBuilder findUserByNickname(String nickname) {
		return linkTo(methodOn(controllerClass).findUserByNickname(nickname));
	}

	public WebMvcLinkBuilder findUserByArticleId(Long articleId) {
		return linkTo(methodOn(controllerClass).findUserByArticleId(articleId));
	}
}
