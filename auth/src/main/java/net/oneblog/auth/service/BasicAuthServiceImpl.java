package net.oneblog.auth.service;

import lombok.AllArgsConstructor;
import net.oneblog.api.interfaces.RoleNameDomain;
import net.oneblog.auth.dto.AuthenticationResponseDto;
import net.oneblog.auth.dto.LoginRequestDto;
import net.oneblog.auth.dto.RegistrationRequestDto;
import net.oneblog.auth.entity.AuthEntity;
import net.oneblog.auth.repository.AuthRepository;
import net.oneblog.auth.repository.RoleRepository;
import net.oneblog.email.dto.RegistrationEmailVerification;
import net.oneblog.email.exceptions.InvalidVerificationCodeException;
import net.oneblog.email.service.EmailVerificationService;
import net.oneblog.sharedexceptions.ServiceException;
import net.oneblog.user.entity.UserEntity;
import net.oneblog.user.exceptions.UserNotFoundException;
import net.oneblog.user.repository.UserRepository;
import net.oneblog.user.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final UserService userService;
    private final RoleRepository roleRepository;
    private final EmailVerificationService emailVerificationService;
    private final AuthRepository authRepository;
    private final TokenService tokenService;


    @Override
    @Transactional
    public void register(RegistrationRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ServiceException("User already exists");
        }
        AuthEntity authEntity = new AuthEntity();

        UserEntity user = UserEntity.builder().email(request.getEmail()).name(request.getName())
            .nickname(request.getUsername()).build();
        authEntity.setUserEntity(user);

        authEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        authEntity.setRoleEntities(
            List.of(roleRepository.findByName(RoleNameDomain.ROLE_USER).get()));
        authEntity.setVerificated(false);

        authRepository.save(authEntity);
        emailVerificationService.sendVerificationCode(request.getEmail());
    }

    @Override
    public void verifyEmail(RegistrationEmailVerification request) {
        boolean verified = emailVerificationService.verifyCode(request);
        if (!verified) {
            throw new InvalidVerificationCodeException("Code is invalid");
        }
        AuthEntity entity =
            authRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ServiceException("User not found"));
        entity.setVerificated(true);

        authRepository.save(entity);
    }


    @Override
    public AuthenticationResponseDto authenticate(LoginRequestDto request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        UserEntity userEntity =
            userRepository.findByNickname(request.getUsername())
                .orElseThrow(() -> new UserNotFoundException(
                    "User with username " + request.getUsername() + " not found"));

        String accessToken = jwtService.generateAccessToken(userEntity);
        String refreshToken = jwtService.generateRefreshToken(userEntity);

        tokenService.revokeAllTokensForUser(userEntity);
        tokenService.saveUserToken(accessToken, refreshToken, userEntity);

        return new AuthenticationResponseDto(accessToken, refreshToken);
    }
}
