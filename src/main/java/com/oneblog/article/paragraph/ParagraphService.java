package com.oneblog.article.paragraph;

import com.oneblog.exceptions.ApiRequestException;

import java.util.List;

public interface ParagraphService {

	public Paragraph save(Paragraph paragraph) throws ApiRequestException;

	public Paragraph findByParagraphId(Long paragraphId) throws ParagraphNotFoundExceptionException;

	public List<Paragraph> findByArticleId(Long articleId) throws ParagraphNotFoundExceptionException;

	public List<Paragraph> findByArticlePreviewId(Long articlePreviewId) throws ParagraphNotFoundExceptionException;

	public Paragraph deleteByParagraphId(Long paragraphId) throws ParagraphNotFoundExceptionException;

}
