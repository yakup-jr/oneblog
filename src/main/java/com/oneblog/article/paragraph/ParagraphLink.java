package com.oneblog.article.paragraph;

import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ParagraphLink {

	private final Class<ParagraphController> controllerClass = ParagraphController.class;

	public WebMvcLinkBuilder findParagraphById(Long paragraphId) {
		return linkTo(methodOn(controllerClass).findParagraphById(paragraphId));
	}

	public WebMvcLinkBuilder findParagraphByArticleId(Long articleId) {
		return linkTo(methodOn(controllerClass).findParagraphsByArticleId(articleId));
	}

	public WebMvcLinkBuilder findParagraphByArticlePreviewId(Long articlePreviewId) {
		return linkTo(methodOn(controllerClass).findParagraphsByArticlePreviewId(articlePreviewId));
	}

}
