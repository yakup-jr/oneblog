package com.oneblog.user;

import com.oneblog.article.Article;
import com.oneblog.auth.Token;
import com.oneblog.user.role.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "T_USER")
public class User implements UserDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ID", unique = true, updatable = false, nullable = false)
	private Long userId;

	@Column(name = "NAME")
	private String name;

	@Column(name = "NICKNAME", unique = true, nullable = false)
	private String nickname;

	@Column(name = "EMAIL", unique = true, nullable = false)
	private String email;

	@Column(name = "PASSWORD")
	private String password;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "T_USER_ROLE", joinColumns = @JoinColumn(name = "USER_ID"),
		inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
	private List<Role> roles;

	@OneToMany(mappedBy = "user")
	private List<Token> tokens;

	@Column(name = "verificated")
	private Boolean verificated;

	@Column(name = "google_user_id", unique = true)
	private String googleUserId;

	@OneToMany(mappedBy = "user")
	private List<Article> articles;

	@Override
	public final boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		Class<?> oEffectiveClass =
			o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() :
				o.getClass();
		Class<?> thisEffectiveClass = this instanceof HibernateProxy ?
			                              ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() :
			                              this.getClass();
		if (thisEffectiveClass != oEffectiveClass) {
			return false;
		}
		User user = (User) o;
		return getUserId() != null && Objects.equals(getUserId(), user.getUserId());
	}

	@Override
	public final int hashCode() {
		return this instanceof HibernateProxy ?
			       ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() :
			       getClass().hashCode();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName().toString())).toList();
	}

	@Override
	public String getUsername() {
		return nickname;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return verificated;
	}
}
