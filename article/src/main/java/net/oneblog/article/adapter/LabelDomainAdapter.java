package net.oneblog.article.adapter;

import net.oneblog.api.interfaces.ArticleDomain;
import net.oneblog.api.interfaces.LabelDomain;
import net.oneblog.article.entity.LabelEntity;

import java.util.List;

public class LabelDomainAdapter implements LabelDomain {
    private final LabelEntity entity;

    public LabelDomainAdapter(LabelEntity entity) {
        this.entity = entity;
    }


    @Override
    public Long getLabelId() {
        return entity.getLabelId();
    }

    @Override
    public String getName() {
        return entity.getName().toString();
    }

    @Override
    public List<ArticleDomain> getArticles() {
        return List.of(entity.getArticleEntities().stream().map(ArticleDomainAdapter::new)
            .toArray(ArticleDomain[]::new));
    }
}
