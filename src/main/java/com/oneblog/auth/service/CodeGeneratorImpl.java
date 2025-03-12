package com.oneblog.auth.service;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class CodeGeneratorImpl implements CodeGenerator {

	private final Random random;

	public CodeGeneratorImpl(Random random) {
		this.random = random;
	}

	@Override
	public String generateSixDigits() {
		return String.format("%06d", random.nextInt(999999));
	}
}
