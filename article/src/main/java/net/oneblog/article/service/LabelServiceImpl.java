package net.oneblog.article.service;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * The type Label service.
 */
@Service
public class LabelServiceImpl implements LabelService {

    private final LabelRepository labelRepository;

    /**
     * Instantiates a new Label service.
     *
     * @param labelRepository the label repository
     */
    public LabelServiceImpl(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }

    @Override
    public LabelEntity save(LabelEntity labelEntity) throws ApiRequestException {
        Optional<LabelEntity> existingLabel = labelRepository.findByName(labelEntity.getName());
        if (existingLabel.isPresent()) {
            throw new ServiceException("Label already exists");
        }
        LabelName newLabelName =
            Arrays.stream(LabelName.values())
                .filter(labelName -> labelName.equals(labelEntity.getName())).findFirst().get();
        return labelRepository.save(LabelEntity.builder().name(newLabelName).build());
    }

    @Override
    public Page<LabelEntity> findAll(Integer page, Integer size) throws PageNotFoundException {
        Pageable pageRequest = PageRequest.of(page, size);
        Page<LabelEntity> labelPage = labelRepository.findAll(pageRequest);
        if (labelPage.isEmpty()) {
            throw new PageNotFoundException("Page" + page + " with size " + size + " not found");
        }
        return labelPage;
    }

    @Override
    public LabelEntity findById(Long labelId) throws LabelNotFoundException {
        return labelRepository.findById(labelId)
            .orElseThrow(
                () -> new LabelNotFoundException("Label with id " + labelId + " not found"));
    }

    @Override
    public LabelEntity findByName(String name) throws LabelNotFoundException {
        try {
            return labelRepository.findByName(LabelName.valueOf(name)).orElseThrow(
                () -> new LabelNotFoundException("Label with name " + name + " not found"));
        } catch (IllegalArgumentException e) {
            throw new LabelNotFoundException("Label with name " + name + " not found");
        }

    }

    @Override
    public List<LabelEntity> findLabels(List<LabelEntity> labelEntities)
        throws LabelNotFoundException {
        List<LabelEntity> foundLabelEntities = new ArrayList<>();
        for (LabelEntity labelEntity : labelEntities) {
            foundLabelEntities.add(findById(labelEntity.getLabelId()));
        }
        return foundLabelEntities;
    }

    @Override
    public LabelEntity deleteById(Long labelId) throws LabelNotFoundException {
        Optional<LabelEntity> deleteLabel = labelRepository.findById(labelId);
        if (deleteLabel.isPresent()) {
            labelRepository.deleteById(labelId);
            return deleteLabel.get();
        }
        throw new LabelNotFoundException("Label with id " + labelId + " not found");
    }
}
