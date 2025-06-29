package com.oneblog.user.role;

import org.springframework.stereotype.Service;

/**
 * The type Role service.
 */
@Service
public class RoleServiceImpl implements RoleService {

	private final RoleRepository roleRepository;

	/**
	 * Instantiates a new Role service.
	 *
	 * @param roleRepository the role repository
	 */
	public RoleServiceImpl(RoleRepository roleRepository) {this.roleRepository = roleRepository;}

	@Override
	public Role findByName(String name) {
		return roleRepository.findByName(RoleName.valueOf(name))
		                     .orElseThrow(() -> new RoleNotFoundException("Role with name " + name + " not found"));
	}
}
