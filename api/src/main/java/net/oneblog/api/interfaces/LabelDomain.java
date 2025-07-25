package net.oneblog.api.interfaces;

import java.util.List;

public interface LabelDomain {
    Long getLabelId();

    String getName();

    List<ArticleDomain> getArticles();
}
