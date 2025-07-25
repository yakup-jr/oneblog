package net.oneblog.api.interfaces;

import java.time.LocalDateTime;
import java.util.List;

/**
 * The interface Article domain.
 */
public interface ArticleDomain {
    /**
     * Gets article id.
     *
     * @return the article id
     */
    Long getArticleId();

    /**
     * Gets title.
     *
     * @return the title
     */
    String getTitle();

    /**
     * Gets body.
     *
     * @return the body
     */
    String getBody();

    /**
     * Gets created at.
     *
     * @return the created at
     */
    LocalDateTime getCreatedAt();

    /**
     * Gets preview.
     *
     * @return the preview
     */
    PreviewDomain getPreview();

    /**
     * Gets labels.
     *
     * @return the labels
     */
    List<LabelDomain> getLabels();

    /**
     * Gets user.
     *
     * @return the user
     */
    UserDomain getUser();
}
