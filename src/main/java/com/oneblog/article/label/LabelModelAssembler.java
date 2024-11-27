package com.oneblog.article.label;

import com.oneblog.article.label.dto.LabelDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class LabelModelAssembler implements RepresentationModelAssembler<LabelDto, EntityModel<LabelDto>> {


	private final LabelLink labelLink;

	public LabelModelAssembler(LabelLink labelLink) {
		this.labelLink = labelLink;
	}


	@Override
	public EntityModel<LabelDto> toModel(LabelDto labelDto) {
		return EntityModel.of(labelDto, labelLink.findLabelByLabelId(labelDto.getLabelId()).withSelfRel());
	}
}
