package com.roze.petcare.service;

import com.roze.petcare.model.response.UserResponse;

public interface UserService {

    UserResponse getUserById(Long id);
}

