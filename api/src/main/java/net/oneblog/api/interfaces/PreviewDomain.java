package net.oneblog.api.interfaces;

public interface PreviewDomain {
    Long getArticlePreviewId();

    String getBody();

    ArticleDomain getArticle();
}
