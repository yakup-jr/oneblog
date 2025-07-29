package net.oneblog.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import net.oneblog.user.controller.UserController;
import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.server.core.Relation;

/**
 * The type User dto.
 */
@Builder
@Relation(collectionRelation = "users")
public record UserDto(@NotNull @Min(1L) Long userId, @NotNull(groups = UserController.class)
@Length(min = 2, max = 60, groups = UserController.class) String name,
                      @NotNull(groups = UserController.class)
                      @Length(min = 2, max = 60, groups = UserController.class) String nickname,
                      @NotNull(groups = UserController.class)
                      @Email(groups = UserController.class) String email) {
}
