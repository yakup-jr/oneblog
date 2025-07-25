package net.oneblog.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import net.oneblog.user.entity.UserEntity;

import java.util.List;

/**
 * The type Auth entity.
 */
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_auth")
@Entity
public class AuthEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "verificated")
    private boolean verificated;

    @Column(name = "google_user_id", unique = true)
    private String googleUserId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "t_user_role", joinColumns = @JoinColumn(name = "auth_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<RoleEntity> roleEntities;

    @OneToMany(mappedBy = "authEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TokenEntity> tokens;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;
}
