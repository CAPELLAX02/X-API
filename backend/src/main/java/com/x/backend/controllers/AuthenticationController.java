package com.x.backend.controllers;

import com.x.backend.dto.request.RegistrationRequest;
import com.x.backend.models.ApplicationUser;
import com.x.backend.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApplicationUser> register(@RequestBody RegistrationRequest registrationRequest) {
        return ResponseEntity.ok(userService.registerUser(registrationRequest));
    }

}
