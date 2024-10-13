package com.oneblog.user;

import com.oneblog.user.role.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	private final RoleRepository roleRepository;


	public UserServiceImpl(
		UserRepository userRepository, RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}

	@Override
	public User createUser(User user) {
		User savedUser = userRepository.save(user);
		log.info(savedUser.getUserId().toString());
		for (int i = 0; i < user.getRoles().size(); i++) {
			//			Role savedRole = roleRepository.save(user.getRoles().get(i).getName());
			//						userRoleRepository.save(savedUser.getId(), savedRole.getId());
		}
		return null;
	}

	@Override
	public void deleteUser(Long id) {
		//		userRoleRepository.deleteByUserId(id);
		userRepository.deleteById(id);
		roleRepository.deleteById(id);
	}
}
