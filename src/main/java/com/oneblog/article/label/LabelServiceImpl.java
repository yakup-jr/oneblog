package com.oneblog.article.label;

import com.oneblog.exceptions.ApiRequestException;
import com.oneblog.exceptions.PageNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LabelServiceImpl implements LabelService {

	private final LabelRepository labelRepository;

	public LabelServiceImpl(LabelRepository labelRepository) {
		this.labelRepository = labelRepository;
	}

	@Override
	public Label save(Label label) throws ApiRequestException {
		Optional<Label> existingLabel = labelRepository.findByName(label.getName());
		if (existingLabel.isPresent()) {
			throw new ApiRequestException("Label already exists");
		}
		for (LabelName labelName : LabelName.values()) {
			if (labelName.equals(label.getName())) {
				return labelRepository.save(label);
			}
		}
		throw new ApiRequestException("Incorrect data");
	}

	@Override
	public Page<Label> findAll(Integer page, Integer size) throws PageNotFoundException {
		Pageable pageRequest = PageRequest.of(page, size);
		Page<Label> labelPage = labelRepository.findAll(pageRequest);
		if (labelPage.isEmpty()) {
			throw new PageNotFoundException("Page" + page + " with size " + size + " not found");
		}
		return labelPage;
	}

	@Override
	public Label findById(Long labelId) throws LabelNotFoundException {
		return labelRepository.findById(labelId)
		                      .orElseThrow(() -> new LabelNotFoundException("Label with id " + labelId + " not found"));
	}

	@Override
	public Label findByName(String name) throws LabelNotFoundException {
		try {
			return labelRepository.findByName(LabelName.valueOf(name)).orElseThrow(
				() -> new LabelNotFoundException("Label with name " + name + " not found"));
		} catch (IllegalArgumentException e) {
			throw new LabelNotFoundException("Label with name " + name + " not found");
		}

	}

	@Override
	public List<Label> findLabels(List<Label> labels) throws LabelNotFoundException {
		List<Label> foundLabels = new ArrayList<>();
		for (Label label : labels) {
			foundLabels.add(findById(label.getLabelId()));
		}
		return foundLabels;
	}

	@Override
	public Label deleteById(Long labelId) throws LabelNotFoundException {
		Optional<Label> deleteLabel = labelRepository.findById(labelId);
		if (deleteLabel.isPresent()) {
			labelRepository.deleteById(labelId);
			return deleteLabel.get();
		}
		throw new LabelNotFoundException("Label with id " + labelId + " not found");
	}
}
