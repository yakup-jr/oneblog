package com.oneblog.article.label;

import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class LabelLink {

	private final Class<LabelController> controllerClass = LabelController.class;

	public WebMvcLinkBuilder findLabelByLabelId(Long id) {
		return linkTo(methodOn(controllerClass).findLabelByLabelId(id));
	}

	public WebMvcLinkBuilder findLabelByLabelName(String labelName) {
		return linkTo(methodOn(controllerClass).findLabelByLabelName(labelName));
	}

	public WebMvcLinkBuilder findAllLabels(Integer page, Integer size) {
		return linkTo(methodOn(controllerClass).findAllLabels(page, size));
	}

	public WebMvcLinkBuilder findAllLabels() {
		return linkTo(methodOn(controllerClass).findAllLabels(0, 10));
	}


}
