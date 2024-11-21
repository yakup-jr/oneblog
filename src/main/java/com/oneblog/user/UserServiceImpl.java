package com.oneblog.user;

import com.oneblog.exceptions.ApiRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public User save(User user) {
		if (userRepository.existsByEmailOrNickname(user.getEmail(), user.getNickname())) {
			throw new ApiRequestException("User nickname and email must be unique");
		}

		return userRepository.save(user);
	}

	@Override
	public boolean existsById(Long userId) {
		return userRepository.existsById(userId);
	}

	@Override
	public User findById(Long id) {
		return userRepository.findById(id)
		                     .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not " + "found"));
	}

	@Override
	public User findByNickname(String nickname) {
		return userRepository.findByNickname(nickname).orElseThrow(
			() -> new UserNotFoundException("User with nickname " + nickname + " not found"));
	}

	@Override
	public User findByEmail(String email) throws UserNotFoundException {
		return userRepository.findByEmail(email)
		                     .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
	}

	@Override
	public User deleteById(Long id) {
		User user = findById(id);
		userRepository.deleteById(id);
		return user;
	}
}
