package com.oneblog.article.preview;

/**
 * The interface Preview service.
 */
public interface PreviewService {

	/**
	 * Find by id preview.
	 *
	 * @param id the id
	 * @return the preview
	 * @throws PreviewNotFoundException the preview not found exception
	 */
	Preview findById(Long id) throws PreviewNotFoundException;

}
