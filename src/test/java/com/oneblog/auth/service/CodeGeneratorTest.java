package com.oneblog.auth.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CodeGeneratorTest {

	@Mock
	private Random mockRandom;

	@InjectMocks
	private CodeGeneratorImpl codeGenerator;

	@Test
	void testGenerateSixDigits() {
		for (int i = 1; i <= 6; i++) {
			int expectedRandomNumber = Math.toIntExact(Math.round(Math.pow(10, i) - 1));
			when(mockRandom.nextInt(999999)).thenReturn(expectedRandomNumber);

			String result = codeGenerator.generateSixDigits();

			assertEquals(String.format("%06d", expectedRandomNumber), result);
		}
	}
}