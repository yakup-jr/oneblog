package net.oneblog.article.adapter;

import net.oneblog.api.interfaces.ArticleDomain;
import net.oneblog.api.interfaces.LabelDomain;
import net.oneblog.api.interfaces.PreviewDomain;
import net.oneblog.api.interfaces.UserDomain;
import net.oneblog.article.entity.ArticleEntity;
import net.oneblog.user.adapter.UserDomainAdapter;

import java.time.LocalDateTime;
import java.util.List;

public class ArticleDomainAdapter implements ArticleDomain {

    private final ArticleEntity entity;

    public ArticleDomainAdapter(ArticleEntity entity) {
        this.entity = entity;
    }


    @Override
    public Long getArticleId() {
        return entity.getArticleId();
    }

    @Override
    public String getTitle() {
        return entity.getTitle();
    }

    @Override
    public String getBody() {
        return entity.getBody();
    }

    @Override
    public LocalDateTime getCreatedAt() {
        return entity.getCreatedAt();
    }

    @Override
    public PreviewDomain getPreview() {
        return new PreviewDomainAdapter(entity.getPreviewEntity());
    }

    @Override
    public List<LabelDomain> getLabels() {
        return List.of(entity.getLabelEntities().stream().map(LabelDomainAdapter::new)
            .toArray(LabelDomain[]::new));
    }

    @Override
    public UserDomain getUser() {
        return new UserDomainAdapter(entity.getUserEntity());
    }
}
