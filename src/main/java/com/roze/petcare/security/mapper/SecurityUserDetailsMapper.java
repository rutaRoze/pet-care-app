package com.roze.petcare.security.mapper;

import com.roze.petcare.persistance.model.RoleEntity;
import com.roze.petcare.persistance.model.UserEntity;
import com.roze.petcare.security.model.SecurityUserDetails;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class SecurityUserDetailsMapper {

    public SecurityUserDetails mapToSecurityUserDetails(UserEntity userEntity) {
        return SecurityUserDetails.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .roleList(userEntity.getRoles().stream()
                        .map(RoleEntity::getName)
                        .collect(Collectors.toSet()))
                .build();
    }
}
