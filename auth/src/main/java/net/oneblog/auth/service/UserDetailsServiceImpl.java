package net.oneblog.auth.service;

import lombok.AllArgsConstructor;
import net.oneblog.auth.adapter.AuthAdapter;
import net.oneblog.auth.entity.AuthEntity;
import net.oneblog.auth.repository.AuthRepository;
import net.oneblog.user.exceptions.UserNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type User details service.
 */
@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AuthRepository authRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        AuthEntity authEntity = authRepository.findByNickname(username).orElseThrow(
            () -> new UserNotFoundException("User with nickname %s not found".formatted(username)));

        return new AuthAdapter(new AuthEntity(authEntity.getAuthId(), authEntity.getPassword(),
            authEntity.isVerificated(), authEntity.getGoogleUserId(), authEntity.getRoleEntities(),
            authEntity.getTokens(), authEntity.getUserEntity()));
    }
}
