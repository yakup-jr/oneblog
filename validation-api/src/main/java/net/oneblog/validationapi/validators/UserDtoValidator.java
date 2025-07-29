package net.oneblog.validationapi.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import net.oneblog.api.dto.UserDto;
import net.oneblog.validationapi.annotations.ValidUserDto;

import java.util.regex.Pattern;

public class UserDtoValidator implements ConstraintValidator<ValidUserDto, UserDto> {
    @Override
    public boolean isValid(UserDto user, ConstraintValidatorContext ctx) {
        if (user == null) return false;
        if (user.userId() == null) return false;
        if (user.nickname() == null || user.nickname().length() < 2 ||
            user.nickname().length() > 60) return false;
        if (user.name() == null || user.name().length() < 2 ||
            user.name().length() > 60) return false;
        if (user.email() == null || !Pattern.matches(user.email(), "^[\\w-\\.]+@([\\w-]+\\.)" +
            "+[\\w-]{2,4}$")) return false;
        return true;
    }
}
