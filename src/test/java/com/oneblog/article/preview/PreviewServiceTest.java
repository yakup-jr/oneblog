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
public class PreviewServiceTest {

	@InjectMocks
	private PreviewServiceImpl articlePreviewService;

	@Mock
	private PreviewRepository previewRepository;

	private static final Preview DEFAULT_PREVIEW = new Preview(1L, "Some body", null);

	@Test
	void findById_ReturnArticlePreview() {
		when(previewRepository.findById(1L)).thenReturn(Optional.of(DEFAULT_PREVIEW));

		Preview preview = articlePreviewService.findById(1L);

		assertThat(preview).isNotNull().isInstanceOf(Preview.class);
		assertThat(preview.getArticlePreviewId()).isEqualTo(DEFAULT_PREVIEW.getArticlePreviewId());
		assertThat(preview).isEqualTo(DEFAULT_PREVIEW);
	}

	@Test
	void findById_ThrowPreviewNotFoundException() {
		when(previewRepository.findById(999L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> articlePreviewService.findById(999L)).isInstanceOf(PreviewNotFoundException.class);
	}

}
