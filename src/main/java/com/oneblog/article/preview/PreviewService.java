package com.oneblog.article.preview;

public interface PreviewService {

	Preview findById(Long id) throws PreviewNotFoundException;

}
