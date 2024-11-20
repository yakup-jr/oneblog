package com.oneblog.article.preview;

import org.springframework.stereotype.Service;

@Service
public class ArticlePreviewServiceImpl implements ArticlePreviewService {

	private ArticlePreviewRepository articlePreviewRepository;

	public ArticlePreviewServiceImpl(ArticlePreviewRepository articlePreviewRepository) {
		this.articlePreviewRepository = articlePreviewRepository;
	}

	@Override
	public ArticlePreview findById(Long id) throws PreviewNotFoundException {
		return articlePreviewRepository.findById(id).orElseThrow(
			() -> new PreviewNotFoundException("Preview with id " + id + " not found"));
	}
}
