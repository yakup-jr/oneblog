package com.oneblog.article.label;

import com.oneblog.article.label.dto.LabelCreateDto;
import com.oneblog.article.label.dto.LabelDto;
import com.oneblog.exceptions.ApiRequestException;
import com.oneblog.exceptions.PageNotFoundException;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/articles")
public class LabelController {

	private final LabelMapper labelMapper;

	private final LabelService labelService;

	private final LabelLink labelLink;

	private final LabelModelAssembler labelModelAssembler;

	private final PagedResourcesAssembler<LabelDto> pagedResourcesAssembler;

	public LabelController(
		LabelMapper labelMapper, LabelService labelService, LabelLink labelLink,
		LabelModelAssembler labelModelAssembler, PagedResourcesAssembler<LabelDto> pagedResourcesAssembler) {
		this.labelMapper = labelMapper;
		this.labelService = labelService;
		this.labelLink = labelLink;
		this.labelModelAssembler = labelModelAssembler;
		this.pagedResourcesAssembler = pagedResourcesAssembler;
	}

	@PostMapping("/label")
	public ResponseEntity<EntityModel<LabelDto>> saveLabel(@RequestBody @Validated LabelCreateDto labelDto) {
		try {
			Label label = labelMapper.map(labelDto);
			Label createLabel = labelService.save(label);
			return ResponseEntity.status(HttpStatus.CREATED).body(EntityModel.of(labelMapper.map(createLabel),
			                                                                     labelLink.findLabelByLabelId(
				                                                                              createLabel.getLabelId())
			                                                                              .withRel("label")));
		} catch (ApiRequestException e) {
			throw new ApiRequestException(e.getMessage());
		}
	}

	@GetMapping("label/{labelId}")
	public ResponseEntity<EntityModel<LabelDto>> findLabelByLabelId(@PathVariable @Validated Long labelId) {
		try {
			Label label = labelService.findById(labelId);
			LabelDto labelDto = labelMapper.map(label);
			Links links = Links.of(labelLink.findLabelByLabelId(labelId).withSelfRel(),
			                       labelLink.findAllLabels().withRel("labels"));
			return ResponseEntity.status(200).body(EntityModel.of(labelDto, links));
		} catch (LabelNotFoundException e) {
			throw new LabelNotFoundException(e.getMessage());
		}
	}

	@GetMapping("label/name/{name}")
	public ResponseEntity<EntityModel<LabelDto>> findLabelByLabelName(@PathVariable @Validated String name) {
		try {
			Label labelByName = labelService.findByName(name);
			LabelDto labelDto = labelMapper.map(labelByName);
			Links links = Links.of(labelLink.findLabelByLabelName(name).withSelfRel(),
			                       labelLink.findAllLabels().withRel("labels"));
			return ResponseEntity.status(HttpStatus.OK).body(EntityModel.of(labelDto, links));
		} catch (LabelNotFoundException e) {
			throw new LabelNotFoundException(e.getMessage());
		}
	}

	@GetMapping("/labels")
	public ResponseEntity<PagedModel<EntityModel<LabelDto>>> findAllLabels(
		@RequestParam @Validated @Min(0L) Integer page,
		@RequestParam(required = false, defaultValue = "10") @Validated @Min(1) @Max(50) Integer size) {
		try {
			Page<LabelDto> labelPage = labelService.findAll(page, size).map(labelMapper::map);
			return ResponseEntity.status(HttpStatus.OK)
			                     .body(pagedResourcesAssembler.toModel(labelPage, labelModelAssembler));
		} catch (PageNotFoundException e) {
			throw new PageNotFoundException(e.getMessage());
		}
	}

	@DeleteMapping("/label/{labelId}")
	public ResponseEntity<EntityModel<LabelDto>> deleteLabel(@PathVariable @Validated Long labelId) {
		try {
			Label label = labelService.deleteById(labelId);
			LabelDto labelDto = labelMapper.map(label);
			return ResponseEntity.status(HttpStatus.OK).body(
				EntityModel.of(labelDto, labelLink.findLabelByLabelId(labelId).withRel("label")));
		} catch (LabelNotFoundException e) {
			throw new LabelNotFoundException(e.getMessage());
		}
	}
}
