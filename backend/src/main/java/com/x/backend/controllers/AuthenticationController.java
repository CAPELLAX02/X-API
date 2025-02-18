package com.x.backend.controllers;

import com.x.backend.dto.request.RegistrationRequest;
import com.x.backend.models.ApplicationUser;
import com.x.backend.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApplicationUser> registerUser(@RequestBody RegistrationRequest registrationRequest) {
        return ResponseEntity.ok(userService.registerUser(registrationRequest));
    }

    @PutMapping("/update/phone")
    public ResponseEntity<ApplicationUser> updatePhoneNumber(@RequestBody LinkedHashMap<String, String> updatePhoneRequest) {
        String username = updatePhoneRequest.get("username");
        String phoneNumber = updatePhoneRequest.get("phoneNumber");
        ApplicationUser user = userService.getUserByUsername(username);
        user.setPhoneNumber(phoneNumber);
        return ResponseEntity.ok(userService.updateUser(user));
    }

}
