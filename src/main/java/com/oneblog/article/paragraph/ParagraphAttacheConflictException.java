package com.oneblog.article.paragraph;

import com.oneblog.exceptions.ConflictException;

public class ParagraphAttacheConflictException extends ConflictException {

	public ParagraphAttacheConflictException(String message) {
		super(message);
	}

	public ParagraphAttacheConflictException(String message, Throwable cause) {
		super(message, cause);
	}
}
