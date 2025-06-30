package com.oneblog.article.label;

import com.oneblog.article.label.dto.LabelDto;
import org.springframework.lang.NonNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 * The type Label model assembler.
 */
@Component
public class LabelModelAssembler implements RepresentationModelAssembler<LabelDto, EntityModel<LabelDto>> {


	private final LabelLink labelLink;

	/**
	 * Instantiates a new Label model assembler.
	 *
	 * @param labelLink the label link
	 */
	public LabelModelAssembler(LabelLink labelLink) {
		this.labelLink = labelLink;
	}


	@Override
	@NonNull
	public EntityModel<LabelDto> toModel(@NonNull LabelDto labelDto) {
		return EntityModel.of(labelDto, labelLink.findLabelByLabelId(labelDto.getLabelId()).withSelfRel());
	}
}
