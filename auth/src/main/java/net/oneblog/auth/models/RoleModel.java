package net.oneblog.auth.models;

import jakarta.validation.constraints.NotNull;
import net.oneblog.api.interfaces.RoleNameDomain;
import org.springframework.hateoas.server.core.Relation;

/**
 * The type Role dto.
 */
@Relation(collectionRelation = "roles")
public record RoleModel(@NotNull RoleNameDomain name) {
}
