package net.oneblog.email.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * The type Email verification.
 */
@Entity
@Table(name = "t_email_verification")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class EmailEntity {
    @Id
    @SequenceGenerator(name = "email_verification_seq", sequenceName = "article_sequence",
        initialValue = 10,
        allocationSize = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "email_verification_seq")
    @Column(name = "email_id")
    private Long emailId;

    @Column(name = "code")
    private String code;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "email")
    private String email;
}
