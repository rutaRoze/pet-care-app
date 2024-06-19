package com.roze.petcare.service;

import com.roze.petcare.model.request.UserRequest;
import com.roze.petcare.model.response.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse findUserById(Long id);

    UserResponse saveUser(UserRequest userRequest);

    UserResponse updateUserById(Long id, UserRequest userRequest);

    void deleteUserById(Long id);

    List<UserResponse> findAllUsers();
}

