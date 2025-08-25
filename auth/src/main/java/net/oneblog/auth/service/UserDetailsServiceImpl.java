package net.oneblog.auth.service;

import lombok.AllArgsConstructor;
import net.oneblog.auth.adapter.AuthAdapter;
import net.oneblog.auth.entity.AuthEntity;
import net.oneblog.user.entity.UserEntity;
import net.oneblog.user.mappers.UserMapper;
import net.oneblog.user.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * The type User details service.
 */
@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userMapper.map(userService.findByNickname(username));

        return new AuthAdapter(AuthEntity.builder().userEntity(userEntity).build());
    }
}
