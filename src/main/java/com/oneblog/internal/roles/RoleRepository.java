package com.oneblog.internal.roles;

import java.util.List;
import java.util.Optional;

public interface RoleRepository {

	Role save(RoleName roleName);

	List<Role> findAll();

	List<Role> findByUserId(Long userId);

	Optional<Role> findByName(RoleName roleName);

	Optional<Role> findById(Long id);

	void deleteByUserId(Long userId);

}
