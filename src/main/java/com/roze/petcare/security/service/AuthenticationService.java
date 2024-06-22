package com.roze.petcare.security.service;

import com.roze.petcare.exception.UserAlreadyExist;
import com.roze.petcare.mapper.UserMapper;
import com.roze.petcare.model.request.UserRequest;
import com.roze.petcare.model.response.UserResponse;
import com.roze.petcare.persistance.UserRepository;
import com.roze.petcare.persistance.model.UserEntity;
import com.roze.petcare.security.mapper.SecurityUserDetailsMapper;
import com.roze.petcare.security.model.AuthenticationRequest;
import com.roze.petcare.security.model.AuthenticationResponse;
import com.roze.petcare.service.RoleService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SecurityUserDetailsMapper securityUserDetailsMapper;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthenticationResponse register(UserRequest registerRequest) {

        if (checkIfUserExistsByEmail(registerRequest.getEmail())) {
            throw new UserAlreadyExist(String.format("User with email %s already exists", registerRequest.getEmail()));
        }

        UserEntity userToSave = userMapper.userRequestToUserEntity(registerRequest);

        userToSave.setName(sanitizeData(registerRequest.getName()));
        userToSave.setSurname(sanitizeData(registerRequest.getSurname()));
        userToSave.setEmail(sanitizeData(registerRequest.getEmail()).toLowerCase(Locale.ROOT));
        userToSave.setPassword(passwordEncoder.encode(sanitizeData(registerRequest.getPassword())));
        userToSave.setRoles(roleService.getRoleEntitiesFormRoleNames(registerRequest.getRoleNames()));

        UserEntity savedUser = userRepository.save(userToSave);

        String jwtToken = jwtService.generateToken(securityUserDetailsMapper.mapToSecurityUserDetails(savedUser));
        UserResponse userResponse = userMapper.userEntityToUserResponse(savedUser);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .userResponse(userResponse)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticateRequest) throws Exception {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticateRequest.getEmail(),
                        authenticateRequest.getPassword()
                )
        );

        UserEntity userEntity = userRepository.findByEmail(authenticateRequest.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Entity not hound"));

        String jwtToken = jwtService.generateToken(securityUserDetailsMapper.mapToSecurityUserDetails(userEntity));
        UserResponse userResponse = userMapper.userEntityToUserResponse(userEntity);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .userResponse(userResponse)
                .build();
    }

    private boolean checkIfUserExistsByEmail(String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }

    private String sanitizeData(String data) {
        if (data == null) {
            return null;
        }

        return data.trim();
    }
}
