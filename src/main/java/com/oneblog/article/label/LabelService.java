package com.oneblog.article.label;

import com.oneblog.exceptions.ApiRequestException;
import com.oneblog.exceptions.PageNotFoundException;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * The interface Label service.
 */
public interface LabelService {

	/**
	 * Save label.
	 *
	 * @param label the label
	 * @return the label
	 * @throws ApiRequestException the api request exception
	 */
	Label save(Label label) throws ApiRequestException;

	/**
	 * Find all page.
	 *
	 * @param page the page
	 * @param size the size
	 * @return the page
	 * @throws PageNotFoundException the page not found exception
	 */
	Page<Label> findAll(Integer page, Integer size) throws PageNotFoundException;

	/**
	 * Find by id label.
	 *
	 * @param labelId the label id
	 * @return the label
	 * @throws LabelNotFoundException the label not found exception
	 */
	Label findById(Long labelId) throws LabelNotFoundException;

	/**
	 * Find by name label.
	 *
	 * @param name the name
	 * @return the label
	 * @throws LabelNotFoundException the label not found exception
	 */
	Label findByName(String name) throws LabelNotFoundException;

	/**
	 * Find labels list.
	 *
	 * @param labels the labels
	 * @return the list
	 * @throws LabelNotFoundException the label not found exception
	 */
	List<Label> findLabels(List<Label> labels) throws LabelNotFoundException;

	/**
	 * Delete by id label.
	 *
	 * @param labelId the label id
	 * @return the label
	 * @throws LabelNotFoundException the label not found exception
	 */
	Label deleteById(Long labelId) throws LabelNotFoundException;

}
