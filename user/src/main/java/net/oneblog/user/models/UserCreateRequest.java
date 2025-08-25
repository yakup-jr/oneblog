package net.oneblog.user.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.server.core.Relation;

/**
 * The type User create dto.
 */
@Relation(collectionRelation = "users")
public record UserCreateRequest(@NotNull @Length(min = 2, max = 60) String name,
                                @NotNull @Length(min = 2, max = 60) String nickname,
                                @NotNull @Email String email) {
}

