package net.oneblog.user.service;

import lombok.AllArgsConstructor;
import net.oneblog.sharedexceptions.PageNotFoundException;
import net.oneblog.sharedexceptions.ServiceException;
import net.oneblog.user.exceptions.UserNotFoundException;
import net.oneblog.user.mappers.UserMapper;
import net.oneblog.user.models.UserCreateRequest;
import net.oneblog.user.repository.UserRepository;
import net.oneblog.validationapi.models.ValidatedUserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type User service.
 */
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserValidationService userValidationService;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ValidatedUserModel save(UserCreateRequest user) {
        if (userValidationService.existsByNickname(user.nickname())) {
            throw new ServiceException(
                "User nickname %s already exists".formatted(user.nickname()));
        } else if (userValidationService.existsByEmail(user.email())) {
            throw new ServiceException("User email %s already exists".formatted(user.email()));
        }

        return userMapper.map(userRepository.save(userMapper.map(user)));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ValidatedUserModel> findAll(Integer page, Integer size) {
        Pageable pageRequest = PageRequest.of(page, size);
        Page<ValidatedUserModel> userPage =
            userRepository.findAll(pageRequest).map(userMapper::map);
        if (userPage.isEmpty()) {
            throw new PageNotFoundException("Page %d with size %d not found".formatted(page, size));
        }
        return userPage;
    }

    @Override
    @Transactional(readOnly = true)
    public ValidatedUserModel findById(Long id) {
        return userRepository.findById(id).map(userMapper::map).orElseThrow(
            () -> new UserNotFoundException("User with id %d not found".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public ValidatedUserModel getReferenceByUserId(Long userId) {
        return userMapper.map(userRepository.getReferenceById(userId));
    }

    @Override
    @Transactional(readOnly = true)
    public ValidatedUserModel findByNickname(String nickname) {
        return userRepository.findByNickname(nickname).map(userMapper::map).orElseThrow(
            () -> new UserNotFoundException("User with nickname %s not found".formatted(nickname)));
    }

    @Override
    @Transactional(readOnly = true)
    public ValidatedUserModel findByEmail(String email) {
        return userRepository.findByEmail(email).map(userMapper::map).orElseThrow(
            () -> new UserNotFoundException("User with email %s not found".formatted(email)));
    }
}
