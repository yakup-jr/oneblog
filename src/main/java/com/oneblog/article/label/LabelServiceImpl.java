package com.oneblog.article.label;

import com.oneblog.exceptions.ApiRequestException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LabelServiceImpl implements LabelService {

	private LabelRepository labelRepository;

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
	public List<Label> findAll() {
		return labelRepository.findAll();
	}

	@Override
	public Optional<Label> findById(Long labelId) {
		return labelRepository.findById(labelId);
	}

	@Override
	public Optional<Label> findByName(LabelName name) {
		return labelRepository.findByName(name);
	}

	@Override
	public Label deleteById(Long labelId) {
		Optional<Label> deleteLabel = labelRepository.findById(labelId);
		if (deleteLabel.isPresent()) {
			labelRepository.deleteById(labelId);
			return deleteLabel.get();
		}
		throw new LabelNotFoundException("Label with id " + labelId + " not found");
	}
}
