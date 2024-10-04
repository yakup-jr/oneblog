package com.oneblog.article.label;


import com.oneblog.exceptions.NotFoundException;

public class LabelNotFoundException extends NotFoundException {

	public LabelNotFoundException(String message) {
		super(message);
	}

	public LabelNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
