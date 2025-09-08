package net.oneblog.api.interfaces;

/**
 * The interface Preview domain.
 */
public interface PreviewDomain {
    /**
     * Gets article preview id.
     *
     * @return the article preview id
     */
    Long getArticlePreviewId();

    /**
     * Gets body.
     *
     * @return the body
     */
    String getBody();

    /**
     * Gets article.
     *
     * @return the article
     */
    ArticleDomain getArticle();
}
