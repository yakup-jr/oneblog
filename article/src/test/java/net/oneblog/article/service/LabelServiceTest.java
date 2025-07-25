package net.oneblog.article.service;

import net.oneblog.api.interfaces.LabelName;
import net.oneblog.article.entity.LabelEntity;
import net.oneblog.article.exception.LabelNotFoundException;
import net.oneblog.article.repository.LabelRepository;
import net.oneblog.sharedexceptions.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LabelServiceTest {

    @Mock
    private LabelRepository labelRepository;

    @InjectMocks
    private LabelServiceImpl labelService;

    @Test
    public void saveLabel_ReturnLabel() {
        LabelEntity inputLabelEntity = LabelEntity.builder().name(LabelName.Assembler).build();
        LabelEntity
            outputLabelEntity =
            LabelEntity.builder().name(inputLabelEntity.getName()).labelId(1L).build();

        when(labelRepository.save(any(LabelEntity.class))).thenReturn(outputLabelEntity);

        LabelEntity labelEntity = labelService.save(inputLabelEntity);

        assertThat(labelEntity).isNotNull().isInstanceOf(LabelEntity.class);
        assertThat(labelEntity.getLabelId()).isEqualTo(outputLabelEntity.getLabelId());
        assertThat(labelEntity).isEqualTo(outputLabelEntity);
    }

    @Test
    public void saveLabel_ThrowServiceException() {
        LabelEntity inputLabelEntity = LabelEntity.builder().name(LabelName.Assembler).build();
        LabelEntity
            outputLabelEntity =
            LabelEntity.builder().name(inputLabelEntity.getName()).labelId(0L).build();

        when(labelRepository.findByName(inputLabelEntity.getName())).thenReturn(Optional.of(
            outputLabelEntity));

        assertThatExceptionOfType(ServiceException.class).isThrownBy(() -> labelService.save(
            inputLabelEntity));
    }

    @Test
    public void findById_ReturnOptionalLabel() {
        Optional<LabelEntity> repositoryResponse = Optional.of(
            LabelEntity.builder().labelId(0L).name(LabelName.Assembler).build());

        when(labelRepository.findById(0L)).thenReturn(repositoryResponse);

        LabelEntity labelEntity = labelService.findById(0L);

        assertThat(labelEntity).isInstanceOf(LabelEntity.class).isEqualTo(repositoryResponse.get());
    }

    @Test
    public void findById_ThrowLabelNotFoundException() {
        when(labelRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatExceptionOfType(LabelNotFoundException.class).isThrownBy(
            () -> labelService.findById(999L));
    }

    @Test
    void findByName_ReturnLabel() {
        LabelEntity inputLabelEntity = LabelEntity.builder().name(LabelName.Assembler).build();

        when(labelRepository.findByName(LabelName.Assembler)).thenReturn(Optional.of(
            inputLabelEntity));

        LabelEntity labelEntity = labelService.findByName("Assembler");

        assertThat(labelEntity).isInstanceOf(LabelEntity.class);
        assertThat(labelEntity.getName()).isEqualTo(inputLabelEntity.getName());
    }

    @Test
    void findByName_ThrowLabelNotFoundException() {

        when(labelRepository.findByName(LabelName.Assembler)).thenReturn(Optional.empty());

        assertThatExceptionOfType(LabelNotFoundException.class).isThrownBy(
            () -> labelService.findByName("Assembler"));
    }

    @Test
    public void deleteById_ReturnLabel() {
        Optional<LabelEntity> repositoryResponse = Optional.of(
            LabelEntity.builder().labelId(0L).name(LabelName.Assembler).build());

        when(labelRepository.findById(0L)).thenReturn(repositoryResponse);

        LabelEntity labelEntity = labelService.deleteById(0L);

        assertThat(labelEntity).isNotNull().isInstanceOf(LabelEntity.class)
            .isEqualTo(repositoryResponse.get());
    }

    @Test
    public void deleteById_ThrowExceptionLabelNotFound() {
        when(labelRepository.findById(0L)).thenReturn(Optional.empty());

        assertThatExceptionOfType(LabelNotFoundException.class).isThrownBy(
            () -> labelService.deleteById(0L));
    }

}
