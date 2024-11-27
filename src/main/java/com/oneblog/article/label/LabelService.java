package com.oneblog.article.label;

import com.oneblog.exceptions.ApiRequestException;
import com.oneblog.exceptions.PageNotFoundException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface LabelService {

	Label save(Label label) throws ApiRequestException;

	Page<Label> findAll(Integer page, Integer size) throws PageNotFoundException;

	Label findById(Long labelId) throws LabelNotFoundException;

	Label findByName(String name) throws LabelNotFoundException;

	List<Label> findLabels(List<Label> labels) throws LabelNotFoundException;

	Label deleteById(Long labelId) throws LabelNotFoundException;

}
