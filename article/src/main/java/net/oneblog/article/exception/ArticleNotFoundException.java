package net.oneblog.article.exception;

import net.oneblog.sharedexceptions.NotFoundException;

/**
 * The type Article didn't find an exception.
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
