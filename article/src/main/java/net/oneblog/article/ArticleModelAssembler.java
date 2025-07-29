package net.oneblog.article;

import net.oneblog.article.models.ArticleModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * The type Article model assembler.
 */
@Component
public class ArticleModelAssembler
    implements RepresentationModelAssembler<ArticleModel, EntityModel<ArticleModel>> {

    private final ArticleLink articleLink;

    /**
     * Instantiates a new Article model assembler.
     *
     * @param articleLink the article link
     */
    public ArticleModelAssembler(ArticleLink articleLink) {
        this.articleLink = articleLink;
    }

    @Override
    @NonNull
    public EntityModel<ArticleModel> toModel(@NonNull ArticleModel articleModel) {
        return EntityModel.of(articleModel,
            articleLink.findArticleByArticleId(articleModel.getArticleId()).withSelfRel());
    }
}

