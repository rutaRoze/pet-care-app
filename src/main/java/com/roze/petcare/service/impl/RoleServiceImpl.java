package com.roze.petcare.service.impl;

import com.roze.petcare.enums.RoleName;
import com.roze.petcare.persistance.RoleRepository;
import com.roze.petcare.persistance.model.RoleEntity;
import com.roze.petcare.service.RoleService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Set<RoleEntity> getRoleEntitiesFormRoleNames(Set<RoleName> roleNames) {
        Set<RoleEntity> roleEntities = new HashSet<>();
        for (RoleName roleName : roleNames) {
            RoleEntity roleEntity = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new EntityNotFoundException("Role not found: " + roleName));
            roleEntities.add(roleEntity);
        }

        return roleEntities;
    }
}
