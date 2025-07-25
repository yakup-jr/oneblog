package net.oneblog.auth.service;

import net.oneblog.auth.entity.RoleEntity;

/**
 * The interface Role service.
 */
public interface RoleService {

	/**
	 * Find by name role.
	 *
	 * @param name the name
	 * @return the role
	 */
	RoleEntity findByName(String name);

}
