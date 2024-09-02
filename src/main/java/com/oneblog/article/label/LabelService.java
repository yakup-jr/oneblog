package com.oneblog.article.label;

import com.oneblog.exceptions.ApiRequestException;

import java.util.List;
import java.util.Optional;

public interface LabelService {

	Label save(Label label);

	List<Label> findAll();

	Optional<Label> findById(Long labelId);

	Optional<Label> findByName(LabelName name);

	Label deleteById(Long labelId);

}
