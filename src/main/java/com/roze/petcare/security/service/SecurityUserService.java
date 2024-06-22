package com.roze.petcare.security.service;

import com.roze.petcare.persistance.UserRepository;
import com.roze.petcare.security.mapper.SecurityUserDetailsMapper;
import com.roze.petcare.security.model.SecurityUserDetails;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecurityUserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SecurityUserDetailsMapper securityUserDetailsMapper;


    public SecurityUserDetails findUserByEmail(String email) {
        return securityUserDetailsMapper
                .mapToSecurityUserDetails(userRepository.findByEmail(email)
                        .orElseThrow(() -> new EntityNotFoundException("User not found")));
    }
}
