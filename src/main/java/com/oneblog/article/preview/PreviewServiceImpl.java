package com.oneblog.article.preview;

import org.springframework.stereotype.Service;

@Service
public class PreviewServiceImpl implements PreviewService {

	private final PreviewRepository previewRepository;

	public PreviewServiceImpl(PreviewRepository previewRepository) {
		this.previewRepository = previewRepository;
	}

	@Override
	public Preview findById(Long id) throws PreviewNotFoundException {
		return previewRepository.findById(id).orElseThrow(
			() -> new PreviewNotFoundException("Preview with id " + id + " not found"));
	}
}
