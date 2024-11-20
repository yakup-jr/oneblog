package com.oneblog.article;

import com.oneblog.exceptions.NotFoundException;

public class ArticleNotFoundException extends NotFoundException {
	public ArticleNotFoundException(String message) {
		super(message);
	}
}
