package com.oneblog.user.role;

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
	Role findByName(String name);

}
