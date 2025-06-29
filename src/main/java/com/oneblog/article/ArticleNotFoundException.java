package com.oneblog.article;

import com.oneblog.exceptions.NotFoundException;

/**
 * The type Article not found exception.
 */
public class ArticleNotFoundException extends NotFoundException {
	/**
	 * Instantiates a new Article not found exception.
	 *
	 * @param message the message
	 */
	public ArticleNotFoundException(String message) {
		super(message);
	}
}
