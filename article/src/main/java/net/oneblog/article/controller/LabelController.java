package net.oneblog.article.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import net.oneblog.article.LabelLink;
import net.oneblog.article.LabelModelAssembler;
import net.oneblog.article.dto.LabelCreateDto;
import net.oneblog.article.dto.LabelDto;
import net.oneblog.article.entity.LabelEntity;
import net.oneblog.article.exception.LabelNotFoundException;
import net.oneblog.article.mapper.LabelMapper;
import net.oneblog.article.service.LabelService;
import net.oneblog.sharedexceptions.ApiRequestException;
import net.oneblog.sharedexceptions.PageNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * The type Label controller.
 */
@RestController
@RequestMapping("/api/v1/articles")
public class LabelController {

    private final LabelMapper labelMapper;

    private final LabelService labelService;

    private final LabelLink labelLink;

    private final LabelModelAssembler labelModelAssembler;

    private final PagedResourcesAssembler<LabelDto> pagedResourcesAssembler;

    /**
     * Instantiates a new Label controller.
     *
     * @param labelMapper             the label mapper
     * @param labelService            the label service
     * @param labelLink               the label link
     * @param labelModelAssembler     the label model assembler
     * @param pagedResourcesAssembler the paged resources assembler
     */
    public LabelController(
        LabelMapper labelMapper, LabelService labelService, LabelLink labelLink,
        LabelModelAssembler labelModelAssembler,
        PagedResourcesAssembler<LabelDto> pagedResourcesAssembler) {
        this.labelMapper = labelMapper;
        this.labelService = labelService;
        this.labelLink = labelLink;
        this.labelModelAssembler = labelModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    /**
     * Save label response entity.
     *
     * @param labelDto the label dto
     * @return the response entity
     */
    @PostMapping("/label")
    public ResponseEntity<EntityModel<LabelDto>> saveLabel(
        @RequestBody @Validated LabelCreateDto labelDto) {
        try {
            LabelEntity labelEntity = labelMapper.map(labelDto);
            LabelEntity createLabelEntity = labelService.save(labelEntity);
            return ResponseEntity.status(HttpStatus.CREATED).body(EntityModel.of(labelMapper.map(
                    createLabelEntity),
                labelLink.findLabelByLabelId(
                        createLabelEntity.getLabelId())
                    .withRel("label")));
        } catch (ApiRequestException e) {
            throw new ApiRequestException(e.getMessage());
        }
    }

    /**
     * Find label by label id response entity.
     *
     * @param labelId the label id
     * @return the response entity
     */
    @GetMapping("label/{labelId}")
    public ResponseEntity<EntityModel<LabelDto>> findLabelByLabelId(
        @PathVariable @Validated Long labelId) {
        try {
            LabelEntity labelEntity = labelService.findById(labelId);
            LabelDto labelDto = labelMapper.map(labelEntity);
            Links links = Links.of(labelLink.findLabelByLabelId(labelId).withSelfRel(),
                labelLink.findAllLabels().withRel("labels"));
            return ResponseEntity.status(200).body(EntityModel.of(labelDto, links));
        } catch (LabelNotFoundException e) {
            throw new LabelNotFoundException(e.getMessage());
        }
    }

    /**
     * Find label by label name response entity.
     *
     * @param name the name
     * @return the response entity
     */
    @GetMapping("label/name/{name}")
    public ResponseEntity<EntityModel<LabelDto>> findLabelByLabelName(
        @PathVariable @Validated String name) {
        try {
            LabelEntity labelEntityByName = labelService.findByName(name);
            LabelDto labelDto = labelMapper.map(labelEntityByName);
            Links links = Links.of(labelLink.findLabelByLabelName(name).withSelfRel(),
                labelLink.findAllLabels().withRel("labels"));
            return ResponseEntity.status(HttpStatus.OK).body(EntityModel.of(labelDto, links));
        } catch (LabelNotFoundException e) {
            throw new LabelNotFoundException(e.getMessage());
        }
    }

    /**
     * Find all labels response entity.
     *
     * @param page the page
     * @param size the size
     * @return the response entity
     */
    @GetMapping("/labels")
    public ResponseEntity<PagedModel<EntityModel<LabelDto>>> findAllLabels(
        @RequestParam @Validated @Min(0L) Integer page,
        @RequestParam(required = false, defaultValue = "10") @Validated @Min(1) @Max(50)
        Integer size) {
        try {
            Page<LabelDto> labelPage = labelService.findAll(page, size).map(labelMapper::map);
            return ResponseEntity.status(HttpStatus.OK)
                .body(pagedResourcesAssembler.toModel(labelPage, labelModelAssembler));
        } catch (PageNotFoundException e) {
            throw new PageNotFoundException(e.getMessage());
        }
    }

    /**
     * Delete label response entity.
     *
     * @param labelId the label id
     * @return the response entity
     */
    @DeleteMapping("/label/{labelId}")
    public ResponseEntity<EntityModel<LabelDto>> deleteLabel(
        @PathVariable @Validated Long labelId) {
        try {
            LabelEntity labelEntity = labelService.deleteById(labelId);
            LabelDto labelDto = labelMapper.map(labelEntity);
            return ResponseEntity.status(HttpStatus.OK).body(
                EntityModel.of(labelDto, labelLink.findLabelByLabelId(labelId).withRel("label")));
        } catch (LabelNotFoundException e) {
            throw new LabelNotFoundException(e.getMessage());
        }
    }
}
