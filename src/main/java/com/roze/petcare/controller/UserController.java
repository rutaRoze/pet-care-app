package com.roze.petcare.controller;

import com.roze.petcare.model.response.UserResponse;
import com.roze.petcare.service.UserService;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @Min(1) @PathVariable Long id)
    {
        UserResponse userResponse = userService.getUserById(id);

        return ResponseEntity.ok(userResponse);
    }
}
