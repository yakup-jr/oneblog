package net.oneblog.auth.adapter;

import net.oneblog.api.interfaces.RoleDomain;
import net.oneblog.api.interfaces.RoleNameDomain;
import net.oneblog.auth.entity.RoleEntity;

public class RoleDomainAdapter implements RoleDomain {

    private final RoleEntity entity;

    public RoleDomainAdapter(RoleEntity entity) {
        this.entity = entity;
    }

    @Override
    public Long getRoleId() {
        return entity.getRoleId();
    }

    @Override
    public RoleNameDomain getRoleName() {
        return entity.getName();
    }
}
