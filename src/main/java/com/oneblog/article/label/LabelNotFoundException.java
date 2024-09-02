package com.oneblog.article.label;


import com.oneblog.exceptions.NotFound;

public class LabelNotFoundException extends NotFound {

	public LabelNotFoundException(String message) {
		super(message);
	}

	public LabelNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
