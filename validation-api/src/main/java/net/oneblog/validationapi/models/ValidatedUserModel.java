package net.oneblog.validationapi.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.springframework.hateoas.server.core.Relation;

@Builder
@Relation(collectionRelation = "users")
public record ValidatedUserModel(@NotNull @Min(1L) Long userId,
                                 @NotNull @Size(min = 2, max = 60) String name,
                                 @NotNull @Size(min = 2, max = 60) String nickname,
                                 @NotNull @Email String email) {
}