package com.oneblog.article.label;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The interface Label repository.
 */
@Repository
public interface LabelRepository extends JpaRepository<Label, Long>, PagingAndSortingRepository<Label, Long> {

	/**
	 * Find by name optional.
	 *
	 * @param name the name
	 * @return the optional
	 */
	Optional<Label> findByName(LabelName name);

}
