package com.oneblog.article;

import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ArticleLink {

	private static final Class<ArticleController> controllerClass = ArticleController.class;

	public WebMvcLinkBuilder findArticleByArticleId(Long articleId) {
		return linkTo(methodOn(controllerClass).findArticleByArticleId(articleId));
	}

	public WebMvcLinkBuilder findArticleByUserId(Long userId) {
		return linkTo(methodOn(controllerClass).findArticleByUserId(userId));
	}

}
