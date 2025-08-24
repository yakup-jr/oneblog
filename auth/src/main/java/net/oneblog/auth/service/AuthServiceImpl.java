package net.oneblog.auth.service;

import lombok.AllArgsConstructor;
import net.oneblog.api.interfaces.RoleNameDomain;
import net.oneblog.auth.entity.AuthEntity;
import net.oneblog.auth.mapper.AuthMapper;
import net.oneblog.auth.models.AuthModel;
import net.oneblog.auth.models.BasicRegistrationRequestModel;
import net.oneblog.auth.models.GoogleRegistrationRequestModel;
import net.oneblog.auth.repository.AuthRepository;
import net.oneblog.sharedexceptions.ServiceException;
import net.oneblog.user.entity.UserEntity;
import net.oneblog.user.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final AuthMapper authMapper;
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthModel save(GoogleRegistrationRequestModel googleRegistrationModel) {
        return authMapper.map(authRepository.save(authMapper.map(googleRegistrationModel)));
    }

    @Override
    public AuthModel save(BasicRegistrationRequestModel basicRegistrationModel) {
        if (userService.existsByEmail(basicRegistrationModel.email())) {
            throw new ServiceException(
                "User with" + basicRegistrationModel.email() + "already exists");
        }
        if (userService.existsByNickname(basicRegistrationModel.username())) {
            throw new ServiceException(
                "User with" + basicRegistrationModel.username() + "already exists");
        }

        AuthEntity authEntity = new AuthEntity();
        UserEntity user = UserEntity.builder().email(basicRegistrationModel.email())
            .name(basicRegistrationModel.name())
            .nickname(basicRegistrationModel.username()).build();
        authEntity.setUserEntity(user);
        authEntity.setPassword(passwordEncoder.encode(basicRegistrationModel.password()));
        authEntity.setRoleEntities(
            List.of(roleService.findByName(RoleNameDomain.ROLE_USER.toString())));
        authEntity.setVerificated(false);

        return authMapper.map(authRepository.save(authEntity));
    }

    @Override
    public AuthModel findByEmail(String email) {
        return authMapper.map(
            authRepository.findByUserEntityEmail(email).orElseThrow(
                () -> new ServiceException("user with email " + email + " not found")));
    }

    @Override
    public AuthModel findByGoogleUserId(String googleUserId) {
        return authMapper.map(authRepository.findByGoogleUserId(googleUserId)
            .orElseThrow(() -> new ServiceException("user with google account not found")));
    }

    @Override
    public void update(AuthModel authModel) {
        authRepository.save(authMapper.map(authModel));
    }
}
