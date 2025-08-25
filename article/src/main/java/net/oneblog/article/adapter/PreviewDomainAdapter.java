package net.oneblog.article.adapter;

import net.oneblog.api.interfaces.ArticleDomain;
import net.oneblog.api.interfaces.PreviewDomain;
import net.oneblog.article.entity.PreviewEntity;

/**
 * The type Preview domain adapter.
 */
public class PreviewDomainAdapter implements PreviewDomain {

    private final PreviewEntity entity;

    /**
     * Instantiates a new Preview domain adapter.
     *
     * @param entity the entity
     */
    public PreviewDomainAdapter(PreviewEntity entity) {
        this.entity = entity;
    }

    @Override
    public Long getArticlePreviewId() {
        return entity.getArticlePreviewId();
    }

    @Override
    public String getBody() {
        return entity.getBody();
    }

    @Override
    public ArticleDomain getArticle() {
        return new ArticleDomainAdapter(entity.getArticleEntity());
    }
}
