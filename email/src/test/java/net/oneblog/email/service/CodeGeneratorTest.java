package net.oneblog.email.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class CodeGeneratorTest {

    @Mock
    private Random random;

    @InjectMocks
    private CodeGeneratorImpl codeGenerator;

    @Test
    void generateSixDigits() {
        assertThat(codeGenerator.generateSixDigits()).hasSize(6);
    }
}
