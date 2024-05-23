package com.roze.petcare.service.impl;

import com.roze.petcare.mapper.UserMapper;
import com.roze.petcare.model.response.UserResponse;
import com.roze.petcare.persistance.UserRepository;
import com.roze.petcare.persistance.model.UserEntity;
import com.roze.petcare.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    @Override
    public UserResponse findUserById(Long id) {

        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found by id: " + id));

        return userMapper.userEntityToUserResponse(userEntity);
    }
}