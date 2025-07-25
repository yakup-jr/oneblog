package net.oneblog.api.interfaces;

import java.time.LocalDateTime;
import java.util.List;

public interface ArticleDomain {
    Long getArticleId();

    String getTitle();

    String getBody();

    LocalDateTime getCreatedAt();

    PreviewDomain getPreview();

    List<LabelDomain> getLabels();

    UserDomain getUser();
}
