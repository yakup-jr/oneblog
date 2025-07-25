package net.oneblog.api.interfaces;

import java.util.List;

/**
 * The interface Label domain.
 */
public interface LabelDomain {
    /**
     * Gets label id.
     *
     * @return the label id
     */
    Long getLabelId();

    /**
     * Gets name.
     *
     * @return the name
     */
    String getName();

    /**
     * Gets articles.
     *
     * @return the articles
     */
    List<ArticleDomain> getArticles();
}
