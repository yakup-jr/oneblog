package net.oneblog.user.service;

import net.oneblog.sharedexceptions.PageNotFoundException;
import net.oneblog.sharedexceptions.ServiceException;
import net.oneblog.user.dto.UserCreateDto;
import net.oneblog.api.dto.UserDto;
import net.oneblog.user.exceptions.UserNotFoundException;
import net.oneblog.user.mappers.UserMapper;
import net.oneblog.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * The type User service.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Instantiates a new User service.
     *
     * @param userRepository the user repository
     * @param userMapper     the user mapper
     */
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto save(UserCreateDto user) {
        if (userRepository.existsByNickname(user.nickname())) {
            throw new ServiceException(
                "User nickname " + user.nickname() + " already exists");
        }
        if (userRepository.existsByEmail(user.email())) {
            throw new ServiceException("User email " + user.email() + " already exists");
        }

        return userMapper.map(userRepository.save(userMapper.map(user)));
    }

    @Override
    public boolean existsById(Long userId) {
        return userRepository.existsById(userId);
    }

    @Override
    public Page<UserDto> findAll(Integer page, Integer size) {
        Pageable pageRequest = PageRequest.of(page, size);
        Page<UserDto> userPage = userRepository.findAll(pageRequest).map(userMapper::map);
        if (userPage.isEmpty()) {
            throw new PageNotFoundException("Page " + page + " with size " + size + " not found");
        }
        return userPage;
    }

    @Override
    public UserDto findById(Long id) {
        return userRepository.findById(id).map(userMapper::map)
            .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }

    @Override
    public UserDto findByNickname(String nickname) {
        return userRepository.findByNickname(nickname).map(userMapper::map).orElseThrow(
            () -> new UserNotFoundException("User with nickname " + nickname + " not found"));
    }

    @Override
    public UserDto findByEmail(String email) throws UserNotFoundException {
        return userRepository.findByEmail(email).map(userMapper::map)
            .orElseThrow(
                () -> new UserNotFoundException("User with email " + email + " not found"));
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
