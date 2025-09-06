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
import net.oneblog.user.mappers.UserMapper;
import net.oneblog.user.models.UserCreateRequest;
import net.oneblog.user.service.UserService;
import net.oneblog.user.service.UserValidationService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final AuthMapper authMapper;
    private final UserService userService;
    private final UserValidationService userValidationService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public AuthModel save(GoogleRegistrationRequestModel googleRegistrationModel) {
        return authMapper.map(authRepository.save(authMapper.map(googleRegistrationModel)));
    }

    @Override
    @Transactional
    public AuthModel save(BasicRegistrationRequestModel basicRegistrationModel) {
        if (basicRegistrationModel.email().isEmpty() ||
            userValidationService.existsByEmail(basicRegistrationModel.email())) {
            throw new ServiceException(
                "User with" + basicRegistrationModel.email() + "already exists");
        }
        if (basicRegistrationModel.username().isEmpty() ||
            userValidationService.existsByNickname(basicRegistrationModel.username())) {
            throw new ServiceException(
                "User with" + basicRegistrationModel.username() + "already exists");
        }

        UserCreateRequest userRequest = new UserCreateRequest(
            basicRegistrationModel.name(),
            basicRegistrationModel.username(),
            basicRegistrationModel.email()
        );
        AuthEntity authEntity = new AuthEntity();
        authEntity.setUserEntity(userMapper.map(userRequest));
        authEntity.setPassword(passwordEncoder.encode(basicRegistrationModel.password()));
        authEntity.setRoleEntities(
            List.of(roleService.findByName(RoleNameDomain.ROLE_USER.toString())));
        authEntity.setVerificated(false);

        return authMapper.map(authRepository.save(authEntity));
    }

    @Override
    @Transactional(readOnly = true)
    public AuthModel findByEmail(String email) {
        return authMapper.map(
            authRepository.findByEmail(email).orElseThrow(
                () -> new ServiceException("user with email " + email + " not found")));
    }

    @Override
    @Transactional(readOnly = true)
    public AuthModel findByGoogleUserId(String googleUserId) {
        return authMapper.map(authRepository.findByGoogleUserId(googleUserId)
            .orElseThrow(() -> new ServiceException("user with google account not found")));
    }

    @Override
    @Transactional(readOnly = true)
    public AuthModel findByNickname(String nickname) {
        return authMapper.map(authRepository.findByNickname(nickname).orElseThrow(
            () -> new ServiceException("User with nickname %s not found".formatted(nickname))));
    }

    @Override
    @Transactional
    public void update(AuthModel authModel) {
        authRepository.updateVerificationStatus(authModel.getAuthId(), authModel.isVerificated());
    }
}
