package com.oneblog.internal.users;

import com.oneblog.internal.UserRoleRepository;
import com.oneblog.internal.roles.Role;
import com.oneblog.internal.roles.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	private final RoleRepository roleRepository;

	private final UserRoleRepository userRoleRepository;

	public UserServiceImpl(
			UserRepository userRepository, RoleRepository roleRepository,
			UserRoleRepository userRoleRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.userRoleRepository = userRoleRepository;
	}

	@Override
	public User createUser(User user) {
		User savedUser = userRepository.save(user);
		log.info(savedUser.getId().toString());
		for (int i = 0; i < user.getRoles().size(); i++) {
			Role savedRole = roleRepository.save(user.getRoles().get(i).getName());
			userRoleRepository.save(savedUser.getId(), savedRole.getId());
		}
		return getUser(user.getNickname());
	}

	@Override
	public User getUser(String nickname) {
		Optional<User> user = userRepository.findByNickname(nickname).or(Optional::empty);
		List<Role> roles = roleRepository.findByUserId(user.get().getId());
		user.get().setRoles(roles);
		return user.get();
	}

	@Override
	public void deleteUser(Long id) {
		userRoleRepository.deleteByUserId(id);
		userRepository.deleteById(id);
		roleRepository.deleteByUserId(id);
	}
}
