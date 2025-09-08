package net.oneblog.article.links;

import net.oneblog.article.controller.ArticleController;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * The type Article link.
 */
@Component
public class ArticleLink {

    private static final Class<ArticleController> controllerClass = ArticleController.class;

    /**
     * Find article by article id web mvc link builder.
     *
     * @param articleId the article id
     * @return the web mvc link builder
     */
    public WebMvcLinkBuilder findArticleByArticleId(Long articleId) {
        return linkTo(methodOn(controllerClass).findArticleByArticleId(articleId));
    }

    /**
     * Find article by user id web mvc link builder.
     *
     * @param userId the user id
     * @return the web mvc link builder
     */
    public WebMvcLinkBuilder findArticleByUserId(Long userId) {
        return linkTo(methodOn(controllerClass).findArticleByUserId(userId));
    }

}
