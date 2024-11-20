package com.oneblog.article.label;

import com.oneblog.article.label.dto.LabelCreateDto;
import com.oneblog.article.label.dto.LabelDto;
import com.oneblog.exceptions.ApiRequestException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Links;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/articles")
public class LabelController {

	private final LabelMapper labelMapper;

	private final LabelService labelService;

	private final LabelLink labelLink;

	public LabelController(LabelMapper labelMapper, LabelService labelService, LabelLink labelLink) {
		this.labelMapper = labelMapper;
		this.labelService = labelService;
		this.labelLink = labelLink;
	}

	@PostMapping("/label")
	public ResponseEntity<EntityModel<LabelDto>> createLabel(@RequestBody LabelCreateDto labelDto) {
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
	public ResponseEntity<EntityModel<LabelDto>> findLabelByLabelId(@PathVariable Long labelId) {
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
	public ResponseEntity<EntityModel<LabelDto>> findLabelByLabelName(@PathVariable String name) {
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
	public ResponseEntity<CollectionModel<EntityModel<LabelDto>>> findAllLabels() {
		try {
			List<LabelDto> labels = labelService.findAll().stream().map(labelMapper::map).toList();
			return ResponseEntity.status(HttpStatus.OK).body(
				CollectionModel.of(labels.stream().map(EntityModel::of).toList(),
				                   labelLink.findAllLabels().withSelfRel()));
		} catch (LabelNotFoundException e) {
			throw new LabelNotFoundException(e.getMessage());
		}
	}

	@DeleteMapping("/label/{labelId}")
	public ResponseEntity<EntityModel<LabelDto>> deleteLabel(@PathVariable Long labelId) {
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
