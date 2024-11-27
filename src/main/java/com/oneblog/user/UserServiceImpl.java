package com.oneblog.user;

import com.oneblog.article.ArticleNotFoundException;
import com.oneblog.exceptions.ApiRequestException;
import com.oneblog.exceptions.PageNotFoundException;
import com.oneblog.user.role.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	private final RoleService roleService;

	public UserServiceImpl(UserRepository userRepository, RoleService roleService) {
		this.userRepository = userRepository;
		this.roleService = roleService;
	}

	@Override
	public User save(User user) {

		if (userRepository.existsByNickname(user.getNickname())) {
			throw new ApiRequestException("User nickname " + user.getNickname() + " already exists");
		}
		if (userRepository.existsByEmail(user.getEmail())) {
			throw new ApiRequestException("User email " + user.getEmail() + " already exists");
		}
		user.setRoles(List.of(roleService.findByName("ROLE_USER")));

		return userRepository.save(user);
	}

	@Override
	public boolean existsById(Long userId) {
		return userRepository.existsById(userId);
	}

	@Override
	public Page<User> findAll(Integer page, Integer size) {
		Pageable pageRequest = PageRequest.of(page, size);
		Page<User> userPage = userRepository.findAll(pageRequest);
		if (userPage.isEmpty()) {
			throw new PageNotFoundException("Page " + page + " with size " + size + " not found");
		}
		return userPage;
	}

	@Override
	public User findById(Long id) {
		return userRepository.findById(id)
		                     .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
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
	public User findByArticleId(Long articleId) throws ArticleNotFoundException {
		return userRepository.findUserByArticleId(articleId).orElseThrow(
			() -> new ArticleNotFoundException("Article with id " + articleId + " not found"));
	}

	@Override
	public void deleteById(Long id) {
		if (!userRepository.existsById(id)) {
			throw new UserNotFoundException("User with id " + id + " not found");
		}
		userRepository.deleteById(id);
	}
}
