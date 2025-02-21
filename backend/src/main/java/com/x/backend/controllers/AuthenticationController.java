package com.x.backend.controllers;

import com.x.backend.dto.authentication.request.*;
import com.x.backend.dto.authentication.response.LoginResponse;
import com.x.backend.exceptions.EmailFailedToSentException;
import com.x.backend.models.ApplicationUser;
import com.x.backend.services.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthenticationController {

    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApplicationUser> registerUser(@RequestBody @Valid RegisterUserRequest registerUserRequest) {
        return ResponseEntity.ok(userService.registerUser(registerUserRequest));
    }

    @PutMapping("/update/phone")
    public ResponseEntity<ApplicationUser> updatePhoneNumber(@RequestBody @Valid UpdatePhoneNumberRequest updatePhoneNumberRequest) {
        return ResponseEntity.ok(userService.updateUserPhoneNumber(updatePhoneNumberRequest));
    }

    @PostMapping("/email/code")
    public ResponseEntity<String> startEmailVerification(@RequestBody @Valid StartEmailVerificationRequest startEmailVerificationRequest) throws EmailFailedToSentException {
        return ResponseEntity.ok(userService.startEmailVerification(startEmailVerificationRequest));
    }

    @PostMapping("/email/verify")
    public ResponseEntity<ApplicationUser> completeEmailVerification(@RequestBody @Valid CompleteEmailVerificationRequest completeEmailVerificationRequest) throws EmailFailedToSentException {
        return ResponseEntity.ok(userService.completeEmailVerification(completeEmailVerificationRequest));
    }

    @PutMapping("/update/password")
    public ResponseEntity<ApplicationUser> updatePassword(@RequestBody @Valid UpdatePasswordRequest updatePasswordRequest) {
        return ResponseEntity.ok(userService.updateUserPassword(updatePasswordRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody @Valid LoginRequest loginRequest) {
        LoginResponse loginResponse = userService.loginUser(loginRequest);
        if (loginResponse.accessToken() != null) return ResponseEntity.ok(loginResponse);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginResponse);
    }

}
