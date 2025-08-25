package net.oneblog.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import net.oneblog.api.interfaces.RoleNameDomain;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

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

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        Class<?> oEffectiveClass =
            o instanceof HibernateProxy ?
                ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() :
                o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ?
            ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() :
            this.getClass();
        if (thisEffectiveClass != oEffectiveClass) {
            return false;
        }
        RoleEntity roleEntity = (RoleEntity) o;
        return getRoleId() != null && Objects.equals(getRoleId(), roleEntity.getRoleId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ?
            ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() :
            getClass().hashCode();
    }
}
