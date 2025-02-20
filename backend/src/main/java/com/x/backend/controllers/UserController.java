package com.x.backend.controllers;

import com.x.backend.models.ApplicationUser;
import com.x.backend.services.token.JwtService;
import com.x.backend.services.user.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    public UserController(UserService userService, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @GetMapping("/verify")
    public ResponseEntity<ApplicationUser> verifyIdentity(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken
    ) {
        String username = jwtService.getUsernameFromToken(accessToken);
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

}
