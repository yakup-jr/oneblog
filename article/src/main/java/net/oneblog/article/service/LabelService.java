package net.oneblog.article.service;

import net.oneblog.article.entity.LabelEntity;
import net.oneblog.article.exception.LabelNotFoundException;
import net.oneblog.sharedexceptions.ApiRequestException;
import net.oneblog.sharedexceptions.PageNotFoundException;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * The interface Label service.
 */
public interface LabelService {

    /**
     * Save label.
     *
     * @param labelEntity the label
     * @return the label
     * @throws ApiRequestException the api request exception
     */
    LabelEntity save(LabelEntity labelEntity) throws ApiRequestException;

    /**
     * Find all page.
     *
     * @param page the page
     * @param size the size
     * @return the page
     * @throws PageNotFoundException the page not found exception
     */
    Page<LabelEntity> findAll(Integer page, Integer size) throws PageNotFoundException;

    /**
     * Find by id label.
     *
     * @param labelId the label id
     * @return the label
     * @throws LabelNotFoundException the label not found exception
     */
    LabelEntity findById(Long labelId) throws LabelNotFoundException;

    /**
     * Find by name label.
     *
     * @param name the name
     * @return the label
     * @throws LabelNotFoundException the label not found exception
     */
    LabelEntity findByName(String name) throws LabelNotFoundException;

    /**
     * Find labels list.
     *
     * @param labelEntities the labels
     * @return the list
     * @throws LabelNotFoundException the label not found exception
     */
    List<LabelEntity> findLabels(List<LabelEntity> labelEntities) throws LabelNotFoundException;

    /**
     * Delete by id label.
     *
     * @param labelId the label id
     * @return the label
     * @throws LabelNotFoundException the label not found exception
     */
    LabelEntity deleteById(Long labelId) throws LabelNotFoundException;

}
