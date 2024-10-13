package com.oneblog.article.paragraph;

import com.oneblog.exceptions.NotFoundException;

public class ParagraphNotFoundExceptionException extends NotFoundException {

	public ParagraphNotFoundExceptionException(String message) {
		super(message);
	}

	public ParagraphNotFoundExceptionException(String message, Throwable cause) {
		super(message, cause);
	}

}
