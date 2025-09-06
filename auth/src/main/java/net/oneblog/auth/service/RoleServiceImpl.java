package net.oneblog.auth.service;

import net.oneblog.api.interfaces.RoleNameDomain;
import net.oneblog.auth.entity.RoleEntity;
import net.oneblog.auth.exception.RoleNotFoundException;
import net.oneblog.auth.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public RoleEntity findByName(String name) {
        return roleRepository.findByName(RoleNameDomain.valueOf(name))
            .orElseThrow(() -> new RoleNotFoundException("Role with name " + name + " not found"));
    }
}
