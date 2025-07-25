package net.oneblog.api.interfaces;

/**
 * The interface Role domain.
 */
public interface RoleDomain {
    /**
     * Gets role id.
     *
     * @return the role id
     */
    Long getRoleId();

    /**
     * Gets role name.
     *
     * @return the role name
     */
    RoleNameDomain getRoleName();
}
