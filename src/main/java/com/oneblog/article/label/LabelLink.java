package com.oneblog.article.label;

import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class LabelLink {

	private final Class<LabelController> source = LabelController.class;

	WebMvcLinkBuilder findLabelByLabelId(Long id) {
		return linkTo(methodOn(source).findLabelByLabelId(id));
	}

	WebMvcLinkBuilder findLabelByLabelName(String labelName) {
		return linkTo(methodOn(source).findLabelByLabelName(labelName));
	}

	WebMvcLinkBuilder findAllLabels() {
		return linkTo(methodOn(source).findAllLabels());
	}



}
