package com.oneblog.article.preview;

import com.oneblog.exceptions.NotFoundException;

public class PreviewNotFoundException extends NotFoundException {
	public PreviewNotFoundException(String message) {
		super(message);
	}

	public PreviewNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
