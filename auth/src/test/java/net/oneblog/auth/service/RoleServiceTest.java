package net.oneblog.auth.service;

import net.oneblog.api.interfaces.RoleNameDomain;
import net.oneblog.auth.entity.RoleEntity;
import net.oneblog.auth.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @InjectMocks
    private RoleServiceImpl roleService;

    @Mock
    private RoleRepository roleRepository;

    @Test
    void findByName_Success() {
        String roleName = "ROLE_USER";
        RoleEntity roleEntity = RoleEntity.builder()
            .roleId(1L)
            .name(RoleNameDomain.ROLE_USER)
            .build();

        when(roleRepository.findByName(RoleNameDomain.ROLE_USER)).thenReturn(
            Optional.of(roleEntity));

        RoleEntity result = roleService.findByName(roleName);

        assertNotNull(result);
        assertEquals(roleEntity, result);
        assertEquals(RoleNameDomain.ROLE_USER, result.getName());
    }

    @Test
    void findByName_AdminRole_Success() {
        String roleName = "ROLE_ADMIN";
        RoleEntity roleEntity = RoleEntity.builder()
            .roleId(2L)
            .name(RoleNameDomain.ROLE_ADMIN)
            .build();

        when(roleRepository.findByName(RoleNameDomain.ROLE_ADMIN)).thenReturn(
            Optional.of(roleEntity));

        RoleEntity result = roleService.findByName(roleName);

        assertNotNull(result);
        assertEquals(RoleNameDomain.ROLE_ADMIN, result.getName());
    }

    @Test
    void findByName_InvalidRoleName() {
        String invalidRoleName = "INVALID_ROLE";

        assertThrows(IllegalArgumentException.class, () -> roleService.findByName(invalidRoleName));
    }
}
