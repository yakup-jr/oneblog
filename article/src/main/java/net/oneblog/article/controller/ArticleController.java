package net.oneblog.article.controller;

import lombok.AllArgsConstructor;
import net.oneblog.article.links.ArticleLink;
import net.oneblog.article.links.ArticleModelAssembler;
import net.oneblog.article.models.ArticleCreateModel;
import net.oneblog.article.models.ArticleModel;
import net.oneblog.article.service.ArticleService;
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
@AllArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final ArticleLink articleLink;
    private final ArticleModelAssembler articleModelAssembler;
    private final PagedResourcesAssembler<ArticleModel> pagedResourcesAssembler;

    /**
     * Create article response entity.
     *
     * @param articleDto the article dto
     * @return the response entity
     */
    @PostMapping("/article")
    public ResponseEntity<EntityModel<ArticleModel>> createArticle(
        @RequestBody @Validated ArticleCreateModel articleDto) {
        ArticleModel savedArticleEntity = articleService.save(articleDto);
        List<Link> links = List.of(
            articleLink.findArticleByArticleId(savedArticleEntity.getArticleId())
                .withSelfRel());
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(EntityModel.of(savedArticleEntity, links));
    }

    /**
     * Find article by article id response entity.
     *
     * @param articleId the article id
     * @return the response entity
     */
    @GetMapping("/article/{articleId}")
    public ResponseEntity<EntityModel<ArticleModel>> findArticleByArticleId(
        @PathVariable @Validated Long articleId) {
        ArticleModel foundArticleEntity = articleService.findByArticleId(articleId);
        List<Link> links = List.of(articleLink.findArticleByArticleId(articleId).withSelfRel());
        return ResponseEntity.status(HttpStatus.OK)
            .body(EntityModel.of(foundArticleEntity, links));
    }

    /**
     * Find all articles response entity.
     *
     * @param page the page
     * @param size the size
     * @return the response entity
     */
    @GetMapping("/articles")
    public ResponseEntity<PagedModel<EntityModel<ArticleModel>>> findAllArticles(
        @RequestParam Integer page,
        @RequestParam(required = false, defaultValue = "10") Integer size) {
        Page<ArticleModel> articlesPage =
            articleService.findAll(page, size);
        return ResponseEntity.status(HttpStatus.OK)
            .body(pagedResourcesAssembler.toModel(articlesPage, articleModelAssembler));
    }

    /**
     * Find article by user id response entity.
     *
     * @param userId the user id
     * @return the response entity
     */
    @GetMapping("/article/user/{userId}")
    public ResponseEntity<CollectionModel<EntityModel<ArticleModel>>> findArticleByUserId(
        @PathVariable @Validated Long userId) {
        List<ArticleModel> foundArticleEntities = articleService.findByUserId(userId);
        List<Link> links = List.of(articleLink.findArticleByUserId(userId).withSelfRel());
        List<EntityModel<ArticleModel>> rawArticles = foundArticleEntities.stream().map(
                article -> EntityModel.of(article,
                    articleLink.findArticleByArticleId(article.getArticleId()).withSelfRel()))
            .toList();
        CollectionModel<EntityModel<ArticleModel>> articles =
            CollectionModel.of(rawArticles, links);
        return ResponseEntity.status(HttpStatus.OK).body(articles);
    }

    /**
     * Delete article response entity.
     *
     * @param articleId the article id
     * @return the response entity
     */
    @DeleteMapping("/article/{articleId}")
    public ResponseEntity<Void> deleteArticle(@PathVariable @Validated Long articleId) {
        articleService.deleteByArticleId(articleId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
