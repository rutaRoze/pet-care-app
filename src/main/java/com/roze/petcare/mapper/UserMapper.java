package com.roze.petcare.mapper;

import com.roze.petcare.enums.RoleName;
import com.roze.petcare.model.request.UserRequest;
import com.roze.petcare.model.response.UserResponse;
import com.roze.petcare.persistance.model.RoleEntity;
import com.roze.petcare.persistance.model.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "roleNames", source = "roles")
    UserResponse userEntityToUserResponse(UserEntity userEntity);

    default Set<RoleName> mapRoles(Set<RoleEntity> roles) {
        return roles.stream()
                .map(role -> role.getName())
                .collect(Collectors.toSet());
    }

    @Mapping(target = "roles", source = "roleNames")
    UserEntity userRequestToUserEntity(UserRequest userRequest);

    default Set<RoleEntity> mapRoleNamesToRoles(Set<RoleName> roleNames) {
        return roleNames.stream()
                .map(roleName -> {
                    RoleEntity roleEntity = new RoleEntity();
                    roleEntity.setName(roleName);
                    return roleEntity;
                })
                .collect(Collectors.toSet());
    }
}