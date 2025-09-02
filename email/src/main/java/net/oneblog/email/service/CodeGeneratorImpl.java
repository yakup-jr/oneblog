package net.oneblog.email.service;

import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * The type Code generator.
 */
@Component
public class CodeGeneratorImpl implements CodeGenerator {

    private final Random random = new Random();

    @Override
    public String generateSixDigits() {
        int randomNumber = random.nextInt(999999);
        return String.format("%06d", randomNumber);
    }
}
