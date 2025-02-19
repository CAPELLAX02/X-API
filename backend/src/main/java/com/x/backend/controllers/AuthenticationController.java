package com.x.backend.controllers;

import com.x.backend.dto.request.RegistrationRequest;
import com.x.backend.dto.response.LoginResponse;
import com.x.backend.exceptions.EmailFailedToSentException;
import com.x.backend.models.ApplicationUser;
import com.x.backend.services.JwtService;
import com.x.backend.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationController(UserService userService, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
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

    @PostMapping("/email/code")
    public ResponseEntity<String> createEmailVerification(@RequestBody LinkedHashMap<String, String> createEmailVerificationRequest) throws EmailFailedToSentException {
        userService.generateEmailVerification(createEmailVerificationRequest.get("username"));
        return ResponseEntity.ok("Verification code generated, email sent");
    }

    @PostMapping("/email/verify")
    public ResponseEntity<ApplicationUser> verifyEmail(@RequestBody LinkedHashMap<String, String> verifyEmailRequest) {
        String username = verifyEmailRequest.get("username");
        Long verificationCode = Long.parseLong(verifyEmailRequest.get("verificationCode"));
        return ResponseEntity.ok(userService.verifyEmail(username, verificationCode));
    }

    @PutMapping("/update/password")
    public ResponseEntity<ApplicationUser> updatePassword(@RequestBody LinkedHashMap<String, String> updatePasswordRequest) {
        String username = updatePasswordRequest.get("username");
        String password = updatePasswordRequest.get("password");
        return ResponseEntity.ok(userService.setPassword(username, password));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LinkedHashMap<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            String accessToken = jwtService.generateToken(authentication);
            return ResponseEntity.ok(new LoginResponse(userService.getUserByUsername(username), accessToken));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse(null, ""));
        }
    }

}
