package com.x.backend.controllers;

import com.x.backend.dto.authentication.request.*;
import com.x.backend.dto.authentication.response.LoginResponse;
import com.x.backend.exceptions.EmailFailedToSentException;
import com.x.backend.models.ApplicationUser;
import com.x.backend.services.token.JwtService;
import com.x.backend.services.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
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
    public ResponseEntity<ApplicationUser> registerUser(@RequestBody RegisterUserRequest registerUserRequest) {
        return ResponseEntity.ok(userService.registerUser(registerUserRequest));
    }

    @PutMapping("/update/phone")
    public ResponseEntity<ApplicationUser> updatePhoneNumber(@RequestBody UpdatePhoneNumberRequest updatePhoneNumberRequest) {
        String username = updatePhoneNumberRequest.username();
        String phoneNumber = updatePhoneNumberRequest.phoneNumber();
        ApplicationUser user = userService.getUserByUsername(username);
        user.setPhoneNumber(phoneNumber);
        return ResponseEntity.ok(userService.updateUser(user));
    }

    @PostMapping("/email/code")
    public ResponseEntity<String> startEmailVerification(@RequestBody StartEmailVerificationRequest startEmailVerificationRequest) throws EmailFailedToSentException {
        userService.generateEmailVerification(startEmailVerificationRequest.username());
        return ResponseEntity.ok("Verification code generated, email sent");
    }

    @PostMapping("/email/verify")
    public ResponseEntity<ApplicationUser> completeEmailVerification(@RequestBody CompleteEmailVerificationRequest completeEmailVerificationRequest) {
        String username = completeEmailVerificationRequest.username();
        Long verificationCode = Long.parseLong(completeEmailVerificationRequest.verificationCode());
        return ResponseEntity.ok(userService.verifyEmail(username, verificationCode));
    }

    @PutMapping("/update/password")
    public ResponseEntity<ApplicationUser> updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest) {
        String username = updatePasswordRequest.username();
        String password = updatePasswordRequest.password();
        return ResponseEntity.ok(userService.setPassword(username, password));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.username();
        String password = loginRequest.password();
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            String accessToken = jwtService.generateToken(authentication);
            return ResponseEntity.ok(new LoginResponse(accessToken));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse(null));
        }
    }

}
