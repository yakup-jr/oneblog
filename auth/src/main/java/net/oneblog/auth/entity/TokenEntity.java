package net.oneblog.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * The type Token entity.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_token")
@Entity
public class TokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "token_id", nullable = false)
    private Long tokenId;

    @Column(name = "access_token", nullable = false, unique = true)
    private String accessToken;

    @Column(name = "refresh_token", nullable = false, unique = true)
    private String refreshToken;

    @Column(name = "expire_at", nullable = false)
    private LocalDateTime expireAt;

    @Column(name = "is_revoke", nullable = false)
    private Boolean isRevoke;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auth_id", foreignKey = @ForeignKey(name = "fk_token_user"),
        nullable = false)
    private AuthEntity authEntity;
}
