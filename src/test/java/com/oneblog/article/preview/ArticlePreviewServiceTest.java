package com.oneblog.article.preview;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ArticlePreviewServiceTest {

	@InjectMocks
	private ArticlePreviewServiceImpl articlePreviewService;

	@Mock
	private ArticlePreviewRepository articlePreviewRepository;

	private static final ArticlePreview defaultArticlePreview = new ArticlePreview(1L, "Some body", null);

	@Test
	void findById_ReturnArticlePreview() {
		when(articlePreviewRepository.findById(1L)).thenReturn(Optional.of(defaultArticlePreview));

		ArticlePreview articlePreview = articlePreviewService.findById(1L);

		assertThat(articlePreview).isNotNull().isInstanceOf(ArticlePreview.class);
		assertThat(articlePreview.getArticlePreviewId()).isEqualTo(defaultArticlePreview.getArticlePreviewId());
		assertThat(articlePreview).isEqualTo(defaultArticlePreview);
	}

	@Test
	void findById_ThrowPreviewNotFoundException() {
		when(articlePreviewRepository.findById(999L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> articlePreviewService.findById(999L)).isInstanceOf(PreviewNotFoundException.class);
	}

}
