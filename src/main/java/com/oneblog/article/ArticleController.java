package com.oneblog.article;

import com.oneblog.article.dto.ArticleCreateDto;
import com.oneblog.article.dto.ArticleDto;
import com.oneblog.exceptions.ApiRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The type Article controller.
 */
@RestController
@RequestMapping("/api/v1")
public class ArticleController {

	private final ArticleService articleService;
	private final ArticleMapper articleMapper;
	private final ArticleLink articleLink;
	private final ArticleModelAssembler articleModelAssembler;
	private final PagedResourcesAssembler<ArticleDto> pagedResourcesAssembler;

	/**
	 * Instantiates a new Article controller.
	 *
	 * @param articleService          the article service
	 * @param articleMapper           the article mapper
	 * @param articleLink             the article link
	 * @param articleModelAssembler   the article model assembler
	 * @param pagedResourcesAssembler the paged resources assembler
	 */
	public ArticleController(
		ArticleService articleService, ArticleMapper articleMapper, ArticleLink articleLink,
		ArticleModelAssembler articleModelAssembler, PagedResourcesAssembler<ArticleDto> pagedResourcesAssembler) {
		this.articleService = articleService;
		this.articleMapper = articleMapper;
		this.articleLink = articleLink;
		this.articleModelAssembler = articleModelAssembler;
		this.pagedResourcesAssembler = pagedResourcesAssembler;
	}

	/**
	 * Create article response entity.
	 *
	 * @param articleDto the article dto
	 * @return the response entity
	 */
	@PostMapping("/article/")
	public ResponseEntity<EntityModel<ArticleDto>> createArticle(@RequestBody @Validated ArticleCreateDto articleDto) {
		try {
			Article mappedArticle = articleMapper.map(articleDto);
			Article savedArticle = articleService.save(mappedArticle);
			List<Link> links = List.of(articleLink.findArticleByArticleId(savedArticle.getArticleId()).withSelfRel());
			return ResponseEntity.status(HttpStatus.CREATED)
			                     .body(EntityModel.of(articleMapper.map(savedArticle), links));
		} catch (ApiRequestException e) {
			throw new ApiRequestException(e.getMessage());
		}
	}

	/**
	 * Find article by article id response entity.
	 *
	 * @param articleId the article id
	 * @return the response entity
	 */
	@GetMapping("/article/{articleId}")
	public ResponseEntity<EntityModel<ArticleDto>> findArticleByArticleId(@PathVariable @Validated Long articleId) {
		try {
			Article foundArticle = articleService.findByArticleId(articleId);
			List<Link> links = List.of(articleLink.findArticleByArticleId(articleId).withSelfRel());
			return ResponseEntity.status(HttpStatus.OK).body(EntityModel.of(articleMapper.map(foundArticle), links));
		} catch (ArticleNotFoundException e) {
			throw new ArticleNotFoundException(e.getMessage());
		}
	}

	/**
	 * Find all articles response entity.
	 *
	 * @param page the page
	 * @param size the size
	 * @return the response entity
	 */
	@GetMapping("/articles")
	public ResponseEntity<PagedModel<EntityModel<ArticleDto>>> findAllArticles(
		@RequestParam Integer page, @RequestParam(required = false, defaultValue = "10") Integer size) {
		try {
			Page<ArticleDto> articlesPage = articleService.findAll(page, size).map(articleMapper::map);
			return ResponseEntity.status(HttpStatus.OK)
			                     .body(pagedResourcesAssembler.toModel(articlesPage, articleModelAssembler));
		} catch (ApiRequestException e) {
			throw new ApiRequestException(e.getMessage());
		}
	}

	/**
	 * Find article by user id response entity.
	 *
	 * @param userId the user id
	 * @return the response entity
	 */
	@GetMapping("/article/user/{userId}")
	public ResponseEntity<CollectionModel<EntityModel<ArticleDto>>> findArticleByUserId(
		@PathVariable @Validated Long userId) {
		try {
			List<Article> foundArticles = articleService.findByUserId(userId);
			List<Link> links = List.of(articleLink.findArticleByUserId(userId).withSelfRel());
			List<EntityModel<ArticleDto>> rawArticles = foundArticles.stream().map(
				                                                         article -> EntityModel.of(articleMapper.map(article),
				                                                                                   articleLink.findArticleByArticleId(article.getArticleId()).withSelfRel()))
			                                                         .toList();
			CollectionModel<EntityModel<ArticleDto>> articles = CollectionModel.of(rawArticles, links);
			return ResponseEntity.status(HttpStatus.OK).body(articles);
		} catch (ArticleNotFoundException e) {
			throw new ArticleNotFoundException(e.getMessage());
		}
	}

	/**
	 * Delete article response entity.
	 *
	 * @param articleId the article id
	 * @return the response entity
	 */
	@DeleteMapping("/article/{articleId}")
	public ResponseEntity<Void> deleteArticle(@PathVariable @Validated Long articleId) {
		try {
			articleService.deleteByArticleId(articleId);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (ArticleNotFoundException e) {
			throw new ArticleNotFoundException(e.getMessage());
		}
	}

}
