package net.oneblog.auth.service;

import net.oneblog.auth.adapter.AuthAdapter;
import net.oneblog.auth.entity.AuthEntity;
import net.oneblog.user.entity.UserEntity;
import net.oneblog.user.mappers.UserMapper;
import net.oneblog.user.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserDetailsServiceImpl(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userMapper.map(userService.findByNickname(username));
        AuthEntity authEntity = AuthEntity.builder().userEntity(userEntity).build();

        return new AuthAdapter(authEntity);
    }
}
