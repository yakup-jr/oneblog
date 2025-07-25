package net.oneblog.article;

import net.oneblog.article.controller.LabelController;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * The type Label link.
 */
@Component
public class LabelLink {

	private final Class<LabelController> controllerClass = LabelController.class;

	/**
	 * Find label by label id web mvc link builder.
	 *
	 * @param id the id
	 * @return the web mvc link builder
	 */
	public WebMvcLinkBuilder findLabelByLabelId(Long id) {
		return linkTo(methodOn(controllerClass).findLabelByLabelId(id));
	}

	/**
	 * Find label by label name web mvc link builder.
	 *
	 * @param labelName the label name
	 * @return the web mvc link builder
	 */
	public WebMvcLinkBuilder findLabelByLabelName(String labelName) {
		return linkTo(methodOn(controllerClass).findLabelByLabelName(labelName));
	}

	/**
	 * Find all labels web mvc link builder.
	 *
	 * @param page the page
	 * @param size the size
	 * @return the web mvc link builder
	 */
	public WebMvcLinkBuilder findAllLabels(Integer page, Integer size) {
		return linkTo(methodOn(controllerClass).findAllLabels(page, size));
	}

	/**
	 * Find all labels web mvc link builder.
	 *
	 * @return the web mvc link builder
	 */
	public WebMvcLinkBuilder findAllLabels() {
		return linkTo(methodOn(controllerClass).findAllLabels(0, 10));
	}


}
