package net.oneblog.auth.models;

import net.oneblog.api.interfaces.RoleNameDomain;
import org.springframework.hateoas.server.core.Relation;

/**
 * The type Role dto.
 */
@Relation(collectionRelation = "roles")
public record RoleModel(RoleNameDomain name) {
}
