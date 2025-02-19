package com.x.backend.controllers;

import com.x.backend.dto.request.RegistrationRequest;
import com.x.backend.exceptions.EmailFailedToSentException;
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

    @PostMapping("/email/code")
    public ResponseEntity<String> createEmailVerification(@RequestBody LinkedHashMap<String, String> createEmailVerificationRequest) throws EmailFailedToSentException {
        userService.generateEmailVerification(createEmailVerificationRequest.get("username"));
        return ResponseEntity.ok("Verification code generated, email sent");
    }

    @PostMapping("/email/verify")
    public ResponseEntity<ApplicationUser> verifyEmail(@RequestBody LinkedHashMap<String, String> veriftEmailRequest) {
        String username = veriftEmailRequest.get("username");
        Long verificationCode = Long.parseLong(veriftEmailRequest.get("verificationCode"));
        return ResponseEntity.ok(userService.verifyEmail(username, verificationCode));
    }

    @PutMapping("/update/password")
    public ResponseEntity<ApplicationUser> updatePassword(@RequestBody LinkedHashMap<String, String> updatePasswordRequest) {
        String username = updatePasswordRequest.get("username");
        String password = updatePasswordRequest.get("password");
        return ResponseEntity.ok(userService.setPassword(username, password));
    }

}
