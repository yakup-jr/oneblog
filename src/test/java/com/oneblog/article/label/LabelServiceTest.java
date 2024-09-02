package com.oneblog.article.label;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LabelServiceTest {

	@Mock
	private LabelRepository labelRepository;

	@InjectMocks
	private LabelServiceImpl labelService;

	@Test
	public void saveLabel_ReturnLabel() {
		Label inputLabel = Label.builder().name(LabelName.Assembler).build();
		Label outputLabel = Label.builder().name(inputLabel.getName()).labelId(0L).build();

		when(labelRepository.save(inputLabel)).thenReturn(outputLabel);

		Label label = labelService.save(inputLabel);

		assertThat(label).isNotNull().isInstanceOf(Label.class);
		assertThat(label.getLabelId()).isEqualTo(outputLabel.getLabelId());
		assertThat(label).isEqualTo(outputLabel);
	}

	@Test
	public void findById_ReturnOptionalLabel() {
		Optional<Label> repositoryResponse = Optional.of(Label.builder().labelId(0L).name(LabelName.Assembler).build());

		when(labelRepository.findById(0L)).thenReturn(repositoryResponse);

		Optional<Label> label = labelService.findById(0L);

		assertThat(label.isPresent()).isTrue();
		assertThat(label.get()).isInstanceOf(Label.class).isEqualTo(repositoryResponse.get());
	}

	@Test
	public void findById_ReturnOptionalEmpty() {
		when(labelRepository.findById(999L)).thenReturn(Optional.empty());

		Optional<Label> label = labelService.findById(999L);

		assertThat(label.isPresent()).isFalse();
	}

	@Test
	public void deleteById_ReturnLabel() {
		Optional<Label> repositoryResponse = Optional.of(Label.builder().labelId(0L).name(LabelName.Assembler).build());

		when(labelRepository.findById(0L)).thenReturn(repositoryResponse);

		Label label = labelService.deleteById(0L);

		assertThat(label).isNotNull().isInstanceOf(Label.class).isEqualTo(repositoryResponse.get());
	}

	@Test
	public void deleteById_ThrowExceptionLabelNotFound() {
		when(labelRepository.findById(0L)).thenReturn(Optional.empty());

		assertThatExceptionOfType(LabelNotFoundException.class).isThrownBy(() -> labelService.deleteById(0L));
	}

}
