package com.roze.petcare.service;

import com.roze.petcare.enums.RoleName;
import com.roze.petcare.persistance.model.RoleEntity;

import java.util.Set;

public interface RoleService {

    Set<RoleEntity> getRoleEntitiesFormRoleNames(Set<RoleName> roleNames);
}
