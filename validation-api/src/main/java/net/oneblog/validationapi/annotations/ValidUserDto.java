package net.oneblog.validationapi.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import net.oneblog.validationapi.validators.UserDtoValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Constraint(validatedBy = UserDtoValidator.class)
public @interface ValidUserDto {
    String message() default "Invalid user";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
