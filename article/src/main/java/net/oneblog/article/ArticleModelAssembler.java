package net.oneblog.article;

import net.oneblog.article.dto.ArticleDto;
import net.oneblog.user.UserLink;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * The type Article model assembler.
 */
@Component
public class ArticleModelAssembler
    implements RepresentationModelAssembler<ArticleDto, EntityModel<ArticleDto>> {

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
    public EntityModel<ArticleDto> toModel(@NonNull ArticleDto articleDto) {
        return EntityModel.of(articleDto,
            articleLink.findArticleByArticleId(articleDto.getArticleId()).withSelfRel());
    }
}

