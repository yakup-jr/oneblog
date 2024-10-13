package com.oneblog.article.paragraph;

import com.oneblog.exceptions.ApiRequestException;

import java.util.List;

public interface ParagraphService {

	Paragraph save(Paragraph paragraph) throws ApiRequestException;

	Paragraph findByParagraphId(Long paragraphId) throws ParagraphNotFoundExceptionException;

	List<Paragraph> findByArticleId(Long articleId) throws ParagraphNotFoundExceptionException;

	List<Paragraph> findByArticlePreviewId(Long articlePreviewId) throws ParagraphNotFoundExceptionException;

	Paragraph deleteByParagraphId(Long paragraphId) throws ParagraphNotFoundExceptionException;

}
