package net.oneblog.user.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * The type User.
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "t_user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true, updatable = false, nullable = false)
    private Long userId;

    @Column(name = "name")
    private String name;

    @Column(name = "nickname", unique = true, nullable = false)
    private String nickname;

    @Column(name = "email", unique = true, nullable = false)
    private String email;
}
