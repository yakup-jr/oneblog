package net.oneblog.auth.dto;

import lombok.*;
import net.oneblog.api.dto.RoleApiDto;
import org.springframework.hateoas.server.core.Relation;

/**
 * The type Role dto.
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Relation(collectionRelation = "roles")
public class RoleDto {
    private RoleApiDto apiDto;
}
