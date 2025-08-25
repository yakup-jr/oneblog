package net.oneblog.article.service;

import net.oneblog.article.entity.PreviewEntity;
import net.oneblog.article.exception.PreviewNotFoundException;
import net.oneblog.article.repository.PreviewRepository;
import org.springframework.stereotype.Service;

/**
 * The type Preview service.
 */
@Service
public class PreviewServiceImpl implements PreviewService {

    private final PreviewRepository previewRepository;

    /**
     * Instantiates a new Preview service.
     *
     * @param previewRepository the preview repository
     */
    public PreviewServiceImpl(PreviewRepository previewRepository) {
        this.previewRepository = previewRepository;
    }

    @Override
    public PreviewEntity findById(Long id) throws PreviewNotFoundException {
        return previewRepository.findById(id).orElseThrow(
            () -> new PreviewNotFoundException("Preview with id " + id + " not found"));
    }
}
