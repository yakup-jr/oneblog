package net.oneblog.auth.service;

import lombok.AllArgsConstructor;
import net.oneblog.api.interfaces.RoleNameDomain;
import net.oneblog.auth.models.AuthenticationResponseModel;
import net.oneblog.auth.models.LoginRequestModel;
import net.oneblog.auth.models.RegistrationRequestModel;
import net.oneblog.auth.entity.AuthEntity;
import net.oneblog.auth.repository.AuthRepository;
import net.oneblog.auth.repository.RoleRepository;
import net.oneblog.email.models.RegistrationEmailVerificationModel;
import net.oneblog.email.exceptions.InvalidVerificationCodeException;
import net.oneblog.email.service.EmailVerificationService;
import net.oneblog.sharedexceptions.ServiceException;
import net.oneblog.user.entity.UserEntity;
import net.oneblog.user.exceptions.UserNotFoundException;
import net.oneblog.user.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Auth service.
 */
@Service
@AllArgsConstructor
public class BasicAuthServiceImpl implements BasicAuthService {

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmailVerificationService emailVerificationService;
    private final AuthRepository authRepository;
    private final TokenService tokenService;


    @Override
    public void register(RegistrationRequestModel request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ServiceException("User already exists");
        }
        AuthEntity authEntity = new AuthEntity();

        UserEntity user = UserEntity.builder().email(request.email()).name(request.name())
            .nickname(request.username()).build();
        authEntity.setUserEntity(user);

        authEntity.setPassword(passwordEncoder.encode(request.password()));
        authEntity.setRoleEntities(
            List.of(roleRepository.findByName(RoleNameDomain.ROLE_USER).get()));
        authEntity.setVerificated(false);

        authRepository.save(authEntity);
        emailVerificationService.sendVerificationCode(request.email());
    }

    @Override
    public void verifyEmail(RegistrationEmailVerificationModel request) {
        boolean verified = emailVerificationService.verifyCode(request);
        if (!verified) {
            throw new InvalidVerificationCodeException("Code is invalid");
        }
        AuthEntity entity =
            authRepository.findByEmail(request.email())
                .orElseThrow(() -> new ServiceException("User not found"));
        entity.setVerificated(true);

        authRepository.save(entity);
    }


    @Override
    public AuthenticationResponseModel authenticate(LoginRequestModel request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        UserEntity userEntity =
            userRepository.findByNickname(request.username())
                .orElseThrow(() -> new UserNotFoundException(
                    "User with username " + request.username() + " not found"));

        String accessToken = jwtService.generateAccessToken(userEntity);
        String refreshToken = jwtService.generateRefreshToken(userEntity);

        tokenService.revokeAllTokensForUser(userEntity);
        tokenService.saveUserToken(accessToken, refreshToken, userEntity);

        return new AuthenticationResponseModel(accessToken, refreshToken);
    }
}
