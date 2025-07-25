package net.oneblog.article.service;

import net.oneblog.article.entity.PreviewEntity;
import net.oneblog.article.exception.PreviewNotFoundException;
import net.oneblog.article.repository.PreviewRepository;
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

	private static final PreviewEntity
		DEFAULT_PREVIEW_ENTITY = new PreviewEntity(1L, "Some body", null);

	@Test
	void findById_ReturnArticlePreview() {
		when(previewRepository.findById(1L)).thenReturn(Optional.of(DEFAULT_PREVIEW_ENTITY));

		PreviewEntity previewEntity = articlePreviewService.findById(1L);

		assertThat(previewEntity).isNotNull().isInstanceOf(PreviewEntity.class);
		assertThat(previewEntity.getArticlePreviewId()).isEqualTo(DEFAULT_PREVIEW_ENTITY.getArticlePreviewId());
		assertThat(previewEntity).isEqualTo(DEFAULT_PREVIEW_ENTITY);
	}

	@Test
	void findById_ThrowPreviewNotFoundException() {
		when(previewRepository.findById(999L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> articlePreviewService.findById(999L)).isInstanceOf(
            PreviewNotFoundException.class);
	}

}
