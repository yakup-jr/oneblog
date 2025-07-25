package net.oneblog.auth.adapter;

import net.oneblog.auth.entity.AuthEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * The type Auth adapter.
 */
public class AuthAdapter implements UserDetails {
    private final AuthEntity entity;

    /**
     * Instantiates a new Auth adapter.
     *
     * @param authEntity the auth entity
     */
    public AuthAdapter(AuthEntity authEntity) {
        this.entity = authEntity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return entity.getRoleEntities().stream()
            .map(role -> new SimpleGrantedAuthority(role.getName().toString())).toList();
    }

    @Override
    public String getPassword() {
        return entity.getPassword();
    }

    @Override
    public String getUsername() {
        return entity.getUserEntity().getNickname();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
