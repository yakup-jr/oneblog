package net.oneblog.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import net.oneblog.api.interfaces.RoleNameDomain;

/**
 * The type Role.
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "T_ROLE")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROLE_ID", unique = true, updatable = false, nullable = false)
    private Long roleId;

    @Column(name = "NAME", unique = true, nullable = false)
    @Enumerated(value = EnumType.STRING)
    private RoleNameDomain name;
}
