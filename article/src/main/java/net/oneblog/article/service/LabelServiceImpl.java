package net.oneblog.article.service;

import lombok.AllArgsConstructor;
import net.oneblog.api.interfaces.LabelName;
import net.oneblog.article.entity.LabelEntity;
import net.oneblog.article.exception.LabelNotFoundException;
import net.oneblog.article.repository.LabelRepository;
import net.oneblog.sharedexceptions.ApiRequestException;
import net.oneblog.sharedexceptions.PageNotFoundException;
import net.oneblog.sharedexceptions.ServiceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;

/**
 * The type Label service.
 */
@Service
@AllArgsConstructor
public class LabelServiceImpl implements LabelService {

    private final LabelRepository labelRepository;

    @Override
    @Transactional
    public LabelEntity save(LabelEntity labelEntity) throws ApiRequestException {
        Optional<LabelEntity> existingLabel = labelRepository.findByName(labelEntity.getName());
        if (existingLabel.isPresent()) {
            throw new ServiceException("Label already exists");
        }
        Optional<LabelName> newLabelName =
            Arrays.stream(LabelName.values())
                .filter(labelName -> labelName.equals(labelEntity.getName())).findFirst();
        if (newLabelName.isEmpty()) {
            throw new LabelNotFoundException("Label cant be save");
        }
        return labelRepository.save(LabelEntity.builder().name(newLabelName.get()).build());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LabelEntity> findAll(Integer page, Integer size) throws PageNotFoundException {
        Pageable pageRequest = PageRequest.of(page, size);
        Page<LabelEntity> labelPage = labelRepository.findAll(pageRequest);
        if (labelPage.isEmpty()) {
            throw new PageNotFoundException("Page" + page + " with size " + size + " not found");
        }
        return labelPage;
    }

    @Override
    @Transactional(readOnly = true)
    public LabelEntity findById(Long labelId) throws LabelNotFoundException {
        return labelRepository.findById(labelId)
            .orElseThrow(
                () -> new LabelNotFoundException("Label with id " + labelId + " not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public LabelEntity findByName(String name) throws LabelNotFoundException {
        try {
            return labelRepository.findByName(LabelName.valueOf(name)).orElseThrow(
                () -> new LabelNotFoundException("Label with name " + name + " not found"));
        } catch (IllegalArgumentException e) {
            throw new LabelNotFoundException("Label with name " + name + " not found");
        }

    }

    @Override
    @Transactional
    public LabelEntity deleteById(Long labelId) throws LabelNotFoundException {
        Optional<LabelEntity> deleteLabel = labelRepository.findById(labelId);
        if (deleteLabel.isPresent()) {
            labelRepository.deleteById(labelId);
            return deleteLabel.get();
        }
        throw new LabelNotFoundException("Label with id " + labelId + " not found");
    }
}
