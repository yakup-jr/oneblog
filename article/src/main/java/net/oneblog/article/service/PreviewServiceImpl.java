package net.oneblog.article.service;

import lombok.AllArgsConstructor;
import net.oneblog.article.entity.PreviewEntity;
import net.oneblog.article.exception.PreviewNotFoundException;
import net.oneblog.article.repository.PreviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type Preview service.
 */
@Service
@AllArgsConstructor
public class PreviewServiceImpl implements PreviewService {

    private final PreviewRepository previewRepository;

    @Override
    @Transactional(readOnly = true)
    public PreviewEntity findById(Long id) throws PreviewNotFoundException {
        return previewRepository.findById(id).orElseThrow(
            () -> new PreviewNotFoundException("Preview with id " + id + " not found"));
    }
}
