package net.oneblog.article.service;

import net.oneblog.article.entity.PreviewEntity;
import net.oneblog.article.exception.PreviewNotFoundException;

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
	PreviewEntity findById(Long id) throws PreviewNotFoundException;

}
