package com.oneblog.article.paragraph;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParagraphServiceImpl implements ParagraphService {

	private final ParagraphRepository paragraphRepository;

	public ParagraphServiceImpl(ParagraphRepository paragraphRepository) {
		this.paragraphRepository = paragraphRepository;
	}

	@Override
	public Paragraph save(Paragraph paragraph) {
		if (paragraph.getArticle() == null && paragraph.getArticlePreview() == null) {
			throw new ParagraphAttacheConflictException("paragraph must be attached at least at one entity");
		}
		if (paragraph.getArticle() != null && paragraph.getArticlePreview() != null) {
			throw new ParagraphAttacheConflictException(
				"paragraph attaching conflict: must by attach only to one entity");
		}
		return paragraphRepository.save(paragraph);
	}

	@Override
	public Paragraph findByParagraphId(Long paragraphId) {
		return paragraphRepository.findById(paragraphId).orElseThrow(
			() -> new ParagraphNotFoundExceptionException("Paragraph with id " + paragraphId + " not found"));
	}

	@Override
	public List<Paragraph> findByArticleId(Long articleId) {
		List<Paragraph> paragraphs = paragraphRepository.findByArticleId(articleId);
		if (paragraphs.isEmpty()) {
			throw new ParagraphNotFoundExceptionException(
				"Paragraph " + "with article" + " id" + articleId + " not found");
		}
		return paragraphs;
	}

	@Override
	public List<Paragraph> findByArticlePreviewId(Long articlePreviewId) {
		return paragraphRepository.findByArticlePreviewId(articlePreviewId).orElseThrow(
			() -> new ParagraphNotFoundExceptionException(
				"Paragraph with articlePreviewId " + articlePreviewId + " not found"));
	}

	@Override
	public Paragraph deleteByParagraphId(Long paragraphId) {
		Paragraph paragraph = paragraphRepository.findById(paragraphId).orElseThrow(
			() -> new ParagraphNotFoundExceptionException("Paragraph with id " + paragraphId + " not found"));
		paragraphRepository.deleteById(paragraphId);
		return paragraph;
	}
}
