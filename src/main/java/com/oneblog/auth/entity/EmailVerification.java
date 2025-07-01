package com.oneblog.auth.entity;

import com.oneblog.user.User;
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
public class EmailVerification {
	@Id
	@SequenceGenerator(name = "email_verification_seq", sequenceName = "article_sequence", initialValue = 10,
		allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "email_verification_seq")
	@Column(name = "id")
	private Long id;

	@Column(name = "code")
	private String code;

	@Column(name = "expires_at")
	private LocalDateTime expiresAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_email_verification_user"), nullable = false)
	private User user;
}
