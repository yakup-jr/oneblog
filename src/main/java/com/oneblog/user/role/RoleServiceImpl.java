package com.oneblog.user.role;

import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

	private final RoleRepository roleRepository;

	public RoleServiceImpl(RoleRepository roleRepository) {this.roleRepository = roleRepository;}

	@Override
	public Role findByName(String name) {
		return roleRepository.findByName(RoleName.valueOf(name))
		                     .orElseThrow(() -> new RoleNotFoundException("Role with name " + name + " not found"));
	}
}
