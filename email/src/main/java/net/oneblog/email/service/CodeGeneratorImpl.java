package net.oneblog.email.service;

import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

/**
 * The type Code generator.
 */
@Component
public class CodeGeneratorImpl implements CodeGenerator {

    @Override
    public String generateSixDigits() {
        int randomNumber = ThreadLocalRandom.current().nextInt(999999);
        return String.format("%06d", randomNumber);
    }
}
