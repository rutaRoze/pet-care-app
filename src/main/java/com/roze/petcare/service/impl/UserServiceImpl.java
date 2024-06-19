package com.roze.petcare.service.impl;

import com.roze.petcare.enums.RoleName;
import com.roze.petcare.exception.NoChangesMadeException;
import com.roze.petcare.exception.UserAlreadyExist;
import com.roze.petcare.mapper.UserMapper;
import com.roze.petcare.model.request.UserRequest;
import com.roze.petcare.model.response.UserResponse;
import com.roze.petcare.persistance.UserRepository;
import com.roze.petcare.persistance.model.RoleEntity;
import com.roze.petcare.persistance.model.UserEntity;
import com.roze.petcare.service.RoleService;
import com.roze.petcare.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserResponse saveUser(UserRequest userRequest) {

        if (checkIfUserExistsByEmail(userRequest.getEmail())) {
            throw new UserAlreadyExist(String.format("User with email %s already exists", userRequest.getEmail()));
        }

        UserEntity userToSave = userMapper.userRequestToUserEntity(userRequest);

        userToSave.setName(sanitizeData(userRequest.getName()));
        userToSave.setName(sanitizeData(userRequest.getSurname()));
        userToSave.setRoles(roleService.getRoleEntitiesFormRoleNames(userRequest.getRoleNames()));

        UserEntity savedUser = userRepository.save(userToSave);

        return userMapper.userEntityToUserResponse(savedUser);
    }

    @Override
    public List<UserResponse> findAllUsers() {

        return userRepository.findAll().stream()
                .map(userEntity -> userMapper.userEntityToUserResponse(userEntity))
                .toList();
    }

    @Override
    public UserResponse findUserById(Long id) {
        UserEntity userEntity = getUserByIdOrThrow(id);

        return userMapper.userEntityToUserResponse(userEntity);
    }

    @Override
    public UserResponse updateUserById(Long id, UserRequest userRequest) {
        UserEntity existingUser = getUserByIdOrThrow(id);

        if (isUserDataEqual(existingUser, userRequest)) {
            throw new NoChangesMadeException("User entry was not updated as no changes of entry were made.");
        }

        if (!existingUser.getEmail().equals(userRequest.getEmail()) && checkIfUserExistsByEmail(userRequest.getEmail())) {
            throw new UserAlreadyExist(String.format("User with email %s already exists", userRequest.getEmail()));
        }

        updateUserData(existingUser, userRequest);
        UserEntity savedUser = userRepository.save(existingUser);

        return userMapper.userEntityToUserResponse(savedUser);
    }

    @Override
    public void deleteUserById(Long id) {
        getUserByIdOrThrow(id);
        userRepository.deleteById(id);
    }

    private UserEntity getUserByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found by id: " + id));
    }

    private boolean checkIfUserExistsByEmail(String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }

    private boolean isUserDataEqual(UserEntity existingUser, UserRequest userRequest) {
        return existingUser.getName().equals(sanitizeData(userRequest.getName())) &&
                existingUser.getSurname().equals(sanitizeData(userRequest.getSurname())) &&
                existingUser.getEmail().equals(userRequest.getEmail()) &&
                existingUser.getPhoneNumber().equals(userRequest.getPhoneNumber()) &&
                isRolesEqual(existingUser.getRoles(), userRequest.getRoleNames());
    }

    private boolean isRolesEqual(Set<RoleEntity> existingRoles, Set<RoleName> requestedRoleNames) {
        Set<RoleName> existingRoleNames = existingRoles.stream()
                .map(RoleEntity::getName)
                .collect(Collectors.toSet());

        return existingRoleNames.equals(requestedRoleNames);
    }

    private void updateUserData(UserEntity existingUser, UserRequest userRequest) {
        existingUser.setName(sanitizeData(userRequest.getName()));
        existingUser.setSurname(sanitizeData(userRequest.getSurname()));
        existingUser.setEmail(userRequest.getEmail());
        existingUser.setPhoneNumber(existingUser.getPhoneNumber());
        existingUser.setRoles(roleService.getRoleEntitiesFormRoleNames(userRequest.getRoleNames()));
    }

    private String sanitizeData(String data) {
        return data.trim();
    }
}
