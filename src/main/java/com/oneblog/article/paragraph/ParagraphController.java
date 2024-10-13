package com.oneblog.article.paragraph;

import com.oneblog.article.ArticleLink;
import com.oneblog.article.paragraph.dto.ParagraphCreateDto;
import com.oneblog.article.paragraph.dto.ParagraphDto;
import com.oneblog.article.preview.ArticlePreviewLink;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ParagraphController {

	private final ParagraphLink paragraphLink;

	private final ParagraphService paragraphService;

	private final ParagraphMapper paragraphMapper;

	private final ArticleLink articleLink;

	private final ArticlePreviewLink articlePreviewLink;

	public ParagraphController(
	ParagraphLink paragraphLink, ParagraphService paragraphService, ParagraphMapper paragraphMapper,
	ArticleLink articleLink, ArticlePreviewLink articlePreviewLink) {
		this.paragraphLink = paragraphLink;
		this.paragraphService = paragraphService;
		this.paragraphMapper = paragraphMapper;
		this.articleLink = articleLink;
		this.articlePreviewLink = articlePreviewLink;
	}

	@PostMapping("/paragraph")
	public ResponseEntity<EntityModel<ParagraphDto>> createParagraph(@RequestBody ParagraphCreateDto paragraphDto) {
		try {
			Paragraph mappedParagraph = paragraphMapper.map(paragraphDto);
			Paragraph paragraph = paragraphService.save(mappedParagraph);
			return ResponseEntity.status(HttpStatus.CREATED).body(EntityModel.of(paragraphMapper.map(paragraph),
			                                                                     paragraphLink.findParagraphById(
			                                                                                  paragraph.getParagraphId())
			                                                                                  .withSelfRel()));
		} catch (ParagraphAttacheConflictException e) {
			throw new ParagraphAttacheConflictException(e.getMessage());
		}

	}

	@GetMapping("/paragraph/{paragraphId}")
	public ResponseEntity<EntityModel<ParagraphDto>> findParagraphById(@PathVariable Long paragraphId) {
		Paragraph paragraph = paragraphService.findByParagraphId(paragraphId);
		return ResponseEntity.status(HttpStatus.OK).body(
		EntityModel.of(paragraphMapper.map(paragraph), paragraphLink.findParagraphById(paragraphId).withSelfRel()));
	}

	@GetMapping("/paragraph/article/{articleId}")
	public ResponseEntity<CollectionModel<EntityModel<ParagraphDto>>> findParagraphsByArticleId(
	@PathVariable Long articleId) {
		CollectionModel<EntityModel<ParagraphDto>> paragraphsDto = CollectionModel.of(
		paragraphService.findByArticleId(articleId).stream().map(
		                paragraph -> EntityModel.of(paragraphMapper.map(paragraph),
		                                            paragraphLink.findParagraphById(paragraph.getParagraphId()).withSelfRel()))
		                .toList(), articleLink.findArticleByArticleId(articleId).withRel("article"),
		paragraphLink.findParagraphByArticleId(articleId).withSelfRel());
		return ResponseEntity.status(HttpStatus.OK).body(paragraphsDto);
	}

	@GetMapping("/paragraph/article/preview/{previewId}")
	public ResponseEntity<CollectionModel<EntityModel<ParagraphDto>>> findParagraphsByArticlePreviewId(
	@PathVariable Long previewId) {
		CollectionModel<EntityModel<ParagraphDto>> paragraphsDto = CollectionModel.of(
		paragraphService.findByArticlePreviewId(previewId).stream().map(
		                paragraph -> EntityModel.of(paragraphMapper.map(paragraph),
		                                            paragraphLink.findParagraphById(paragraph.getParagraphId()).withSelfRel()))
		                .toList(), paragraphLink.findParagraphByArticlePreviewId(previewId).withSelfRel(),
		articlePreviewLink.findByPreviewId(previewId).withRel("articlePreview"));
		return ResponseEntity.status(HttpStatus.OK).body(paragraphsDto);
	}

	@DeleteMapping("/paragraph/{paragraphId}")
	public ResponseEntity<EntityModel<ParagraphDto>> deleteParagraphById(@PathVariable Long paragraphId) {
		Paragraph paragraph = paragraphService.deleteByParagraphId(paragraphId);
		EntityModel<ParagraphDto> deleteParagraph = EntityModel.of(paragraphMapper.map(paragraph),
		                                                           paragraphLink.findParagraphById(
		                                                           paragraph.getParagraphId()).withSelfRel(),
		                                                           paragraph.getArticlePreview() == null ?
		                                                           articleLink.findArticleByArticleId(
		                                                                      paragraph.getArticle().getArticleId())
		                                                                      .withRel("article") :
		                                                           articlePreviewLink.findByPreviewId(
		                                                                             paragraph.getArticlePreview().getArticlePreviewId())
		                                                                             .withRel("articlePreview"));
		return ResponseEntity.status(HttpStatus.OK).body(deleteParagraph);
	}


}
