package com.x.backend.controllers;

import com.x.backend.dto.*;
import com.x.backend.services.auth.AuthenticationService;
import com.x.backend.utils.api.BaseApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<BaseApiResponse<UserResponse>> register(@Valid @RequestBody RegisterRequest req) {
        BaseApiResponse<UserResponse> res = authenticationService.register(req).getBody();
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PostMapping("/email/verification/send")
    public ResponseEntity<BaseApiResponse<String>> sendVerificationEmail(@Valid @RequestBody SendVerificationEmailRequest req) {
        BaseApiResponse<String> res = authenticationService.sendVerificationEmail(req).getBody();
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PostMapping("/email/verification/resend")
    public ResponseEntity<BaseApiResponse<String>> resendVerificationEmail(@Valid @RequestBody SendVerificationEmailRequest req) {
        BaseApiResponse<String> res = authenticationService.resendVerificationEmail(req).getBody();
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PutMapping("/verify")
    public ResponseEntity<BaseApiResponse<UserResponse>> completeEmailVerification(@Valid @RequestBody CompleteEmailVerificationRequest req) {
        BaseApiResponse<UserResponse> res = authenticationService.completeEmailVerification(req).getBody();
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PutMapping("/phone")
    public ResponseEntity<BaseApiResponse<UserResponse>> setPhoneNumber(@Valid @RequestBody SetPhoneNumberRequest req) {
        BaseApiResponse<UserResponse> res = authenticationService.setPhoneNumber(req).getBody();
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PutMapping("/update/phone")
    public ResponseEntity<BaseApiResponse<UserResponse>> changePhoneNumber(
            @Valid @RequestBody ChangePhoneNumberRequest req,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String authenticatedUsername = userDetails.getUsername();
        BaseApiResponse<UserResponse> res = authenticationService.changePhoneNumber(authenticatedUsername, req).getBody();
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PutMapping("/password")
    public ResponseEntity<BaseApiResponse<UserResponse>> setPassword(@Valid @RequestBody SetPasswordRequest req) {
        BaseApiResponse<UserResponse> res = authenticationService.setPassword(req).getBody();
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PostMapping("/email/password-recovery/send")
    public ResponseEntity<BaseApiResponse<String>> sendPasswordRecoveryEmail(@Valid @RequestBody SendPasswordRecoveryEmailRequest req) {
        BaseApiResponse<String> res = authenticationService.sendPasswordRecoveryEmail(req).getBody();
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PostMapping("/email/password-recovery/resend")
    public ResponseEntity<BaseApiResponse<String>> resendPasswordRecoveryEmail(@Valid @RequestBody SendPasswordRecoveryEmailRequest req) {
        BaseApiResponse<String> res = authenticationService.resendPasswordRecoveryEmail(req).getBody();
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PutMapping("/recover-password")
    public ResponseEntity<BaseApiResponse<UserResponse>> recoverPassword(@Valid @RequestBody RecoverPasswordRequest req) {
        BaseApiResponse<UserResponse> res = authenticationService.recoverPassword(req).getBody();
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PutMapping("/update/password")
    public ResponseEntity<BaseApiResponse<UserResponse>> changePassword(
            @Valid @RequestBody ChangePasswordRequest req,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String authenticatedUsername = userDetails.getUsername();
        BaseApiResponse<UserResponse> res = authenticationService.changePassword(authenticatedUsername, req).getBody();
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PostMapping("/login")
    public ResponseEntity<BaseApiResponse<AuthTokenResponse>> login(@Valid @RequestBody LoginRequest req) {
        BaseApiResponse<AuthTokenResponse> res = authenticationService.login(req).getBody();
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<BaseApiResponse<AuthTokenResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest req) {
        BaseApiResponse<AuthTokenResponse> res = authenticationService.refreshToken(req).getBody();
        return ResponseEntity.status(res.getStatus()).body(res);
    }


}
