package com.oneblog.article;

import com.oneblog.article.dto.ArticleCreateDto;
import com.oneblog.article.dto.ArticleDto;
import com.oneblog.article.label.LabelNotFoundException;
import com.oneblog.exceptions.ApiRequestException;
import com.oneblog.user.UserNotFoundException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/article")
public class ArticleController {

	private final ArticleService articleService;
	private final ArticleMapper articleMapper;
	private final ArticleLink articleLink;

	public ArticleController(ArticleService articleService, ArticleMapper articleMapper, ArticleLink articleLink) {
		this.articleService = articleService;
		this.articleMapper = articleMapper;
		this.articleLink = articleLink;
	}

	@PostMapping("/")
	public ResponseEntity<EntityModel<ArticleDto>> createArticle(@RequestBody ArticleCreateDto articleDto) {
		try {
			Article mappedArticle = articleMapper.map(articleDto);
			Article savedArticle = articleService.save(mappedArticle);
			List<Link> links = List.of(articleLink.findArticleByArticleId(savedArticle.getArticleId()).withSelfRel());
			return ResponseEntity.status(HttpStatus.CREATED)
			                     .body(EntityModel.of(articleMapper.map(savedArticle), links));
		} catch (ApiRequestException | LabelNotFoundException | UserNotFoundException e) {
			throw new ApiRequestException(e.getMessage());
		}
	}

	@GetMapping("/{articleId}")
	public ResponseEntity<EntityModel<ArticleDto>> findArticleByArticleId(@PathVariable Long articleId) {
		try {
			Article foundArticle = articleService.findByArticleId(articleId);
			List<Link> links = List.of(articleLink.findArticleByArticleId(articleId).withSelfRel());
			return ResponseEntity.status(HttpStatus.OK).body(EntityModel.of(articleMapper.map(foundArticle), links));
		} catch (ArticleNotFoundException e) {
			throw new ArticleNotFoundException(e.getMessage());
		}
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<CollectionModel<EntityModel<ArticleDto>>> findArticleByUserId(@PathVariable Long userId) {
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

	@DeleteMapping("/{articleId}")
	public ResponseEntity<Void> deleteArticle(@PathVariable Long articleId) {
		try {
			articleService.deleteByArticleId(articleId);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		} catch (ArticleNotFoundException e) {
			throw new ArticleNotFoundException(e.getMessage());
		}
	}

}
