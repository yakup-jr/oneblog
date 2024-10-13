package com.oneblog.article.paragraph;


import com.oneblog.article.Article;
import com.oneblog.article.preview.ArticlePreview;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParagraphServiceTest {

	@Mock
	private ParagraphRepository paragraphRepository;

	@InjectMocks
	private ParagraphServiceImpl paragraphService;

	@Test
	public void saveParagraph_ReturnParagraph() {
		Paragraph inputParagraph = Paragraph.builder().text("Lorem ipsum 100").build();
		Article article = Article.builder().articleId(1L).title("The Game of Thrones").build();
		inputParagraph.setArticle(article);

		Paragraph exceptedParagraph = inputParagraph;
		exceptedParagraph.setParagraphId(1L);

		when(paragraphRepository.save(inputParagraph)).thenReturn(exceptedParagraph);

		Paragraph outputParagraph = paragraphService.save(inputParagraph);

		assertThat(outputParagraph).isNotNull().isInstanceOf(Paragraph.class);
		assertThat(outputParagraph.getParagraphId()).isEqualTo(exceptedParagraph.getParagraphId());
		assertThat(outputParagraph.getArticle()).isEqualTo(exceptedParagraph.getArticle());
		assertThat(outputParagraph.getArticlePreview()).isEqualTo(exceptedParagraph.getArticlePreview());
	}

	@Test
	public void saveParagraph_ThrowParagraphAlreadyExistException() {
		Paragraph existingParagraph = Paragraph.builder().text("Lorem ipsum 100").build();
		ArticlePreview articlePreview =
			ArticlePreview.builder().articlePreviewId(1L).paragraph(existingParagraph).build();
		Article article = Article.builder().articleId(1L).title("The Game of Thrones").build();
		existingParagraph.setArticlePreview(articlePreview);
		existingParagraph.setArticle(article);

		Paragraph inputParagraph =
			Paragraph.builder().text("Second paragraph").article(article).articlePreview(articlePreview).build();

		assertThatExceptionOfType(ParagraphAttacheConflictException.class).isThrownBy(
			() -> paragraphService.save(inputParagraph));
	}

	@Test
	public void findParagraphByParagraphId_ReturnParagraph() {
		Paragraph expectedParagraph =
			Paragraph.builder().text("The Game of Thrones: 6 season. Teaser").paragraphId(1L).build();

		when(paragraphRepository.findById(1L)).thenReturn(Optional.of(expectedParagraph));

		Paragraph outputParagraph = paragraphService.findByParagraphId(1L);

		assertThat(outputParagraph).isNotNull().isInstanceOf(Paragraph.class);
		assertThat(outputParagraph.getParagraphId()).isEqualTo(expectedParagraph.getParagraphId());
		assertThat(outputParagraph.getText()).isEqualTo(expectedParagraph.getText());
	}

	@Test
	public void findParagraphByParagraphId_ThrowParagraphNotFoundException() {
		when(paragraphRepository.findById(999L)).thenReturn(Optional.empty());

		assertThatExceptionOfType(ParagraphNotFoundExceptionException.class).isThrownBy(
			() -> paragraphService.findByParagraphId(999L));
	}

	@Test
	public void findParagraphByArticleId_ReturnParagraph() {
		Article article = Article.builder().articleId(1L).build();
		Paragraph inputParagraph =
			Paragraph.builder().text("The Game of Thrones").paragraphId(1L).article(article).build();

		when(paragraphRepository.findByArticleId(1L)).thenReturn((List.of(inputParagraph)));

		List<Paragraph> outputParagraph = paragraphService.findByArticleId(1L);

		assertThat(outputParagraph.getFirst()).isNotNull().isInstanceOf(Paragraph.class);
		assertThat(outputParagraph.get(0).getParagraphId()).isEqualTo(inputParagraph.getParagraphId());
	}

	@Test
	public void findParagraphByArticleId_ThrowParagraphNotFoundException() {
		Article article = Article.builder().articleId(1L).build();
		Paragraph.builder().article(article).paragraphId(1L).build();

		when(paragraphRepository.findByArticleId(999L)).thenReturn(new ArrayList<>());

		assertThatExceptionOfType(ParagraphNotFoundExceptionException.class).isThrownBy(
			() -> paragraphService.findByArticleId(999L));
	}

	@Test
	public void findParagraphByArticlePreviewId_ReturnParagraph() {
		ArticlePreview articlePreview = ArticlePreview.builder().articlePreviewId(1L).build();
		Paragraph inputParagraph = Paragraph.builder().articlePreview(articlePreview).build();

		when(paragraphRepository.findByArticlePreviewId(1L)).thenReturn(Optional.of(List.of(inputParagraph)));

		List<Paragraph> outputParagraph = paragraphService.findByArticlePreviewId(1L);

		assertThat(outputParagraph.getFirst()).isNotNull().isInstanceOf(Paragraph.class);
		assertThat(outputParagraph.getFirst().getParagraphId()).isEqualTo(inputParagraph.getParagraphId());
		assertThat(outputParagraph.getFirst().getArticlePreview()).isEqualTo(inputParagraph.getArticlePreview());
	}

	@Test
	public void deleteByParagraphId_ReturnParagraph() {
		Paragraph inputParagraph = Paragraph.builder().paragraphId(1L).build();

		when(paragraphRepository.findById(inputParagraph.getParagraphId())).thenReturn(Optional.of(inputParagraph));

		Paragraph deleteParagraph = paragraphService.deleteByParagraphId(1L);

		assertThat(deleteParagraph).isNotNull().isInstanceOf(Paragraph.class);
		assertThat(deleteParagraph.getParagraphId()).isEqualTo(inputParagraph.getParagraphId());
	}

	@Test
	public void deleteByParagraphId_ThrowParagraphNotFoundException() {
		Paragraph inputParagraph = Paragraph.builder().paragraphId(1L).build();

		when(paragraphRepository.findById(999L)).thenReturn(Optional.empty());

		assertThatExceptionOfType(ParagraphNotFoundExceptionException.class).isThrownBy(
			() -> paragraphService.deleteByParagraphId(999L));
	}


}
