package com.oneblog.article.preview;

public interface ArticlePreviewService {

	ArticlePreview findById(Long id) throws PreviewNotFoundException;

}
