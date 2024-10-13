package com.oneblog.article.preview;

import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ArticlePreviewLink {

	private static final Class<ArticlePreviewController> contorollerClass = ArticlePreviewController.class;

	public WebMvcLinkBuilder findByPreviewId(Long previewId) {
		return linkTo(methodOn(contorollerClass).findByArticlePreviewId(previewId));
	}

}
