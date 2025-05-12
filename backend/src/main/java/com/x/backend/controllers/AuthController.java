package com.x.backend.controllers;

import com.x.backend.dto.auth.request.*;
import com.x.backend.dto.auth.response.AuthTokenResponse;
import com.x.backend.dto.auth.response.SendPasswordRecoveryEmailResponse;
import com.x.backend.dto.auth.response.SendVerificationEmailResponse;
import com.x.backend.dto.auth.response.StartRegistrationResponse;
import com.x.backend.exceptions.email.EmailFailedToSentException;
import com.x.backend.models.entities.ApplicationUser;
import com.x.backend.services.auth.AuthenticationService;
import com.x.backend.utils.api.BaseApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<BaseApiResponse<StartRegistrationResponse>> startRegistration(@Valid @RequestBody StartRegistrationRequest req){
        BaseApiResponse<StartRegistrationResponse> res = authenticationService.startRegistration(req);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PostMapping("/email/verification/send")
    public ResponseEntity<BaseApiResponse<SendVerificationEmailResponse>> sendVerificationEmail(@Valid @RequestBody SendVerificationEmailRequest req) throws EmailFailedToSentException {
        BaseApiResponse<SendVerificationEmailResponse> res = authenticationService.sendVerificationEmail(req);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PostMapping("/email/verification/resend")
    public ResponseEntity<BaseApiResponse<SendVerificationEmailResponse>> resendVerificationEmail(@Valid @RequestBody SendVerificationEmailRequest req) throws EmailFailedToSentException  {
        BaseApiResponse<SendVerificationEmailResponse> res = authenticationService.resendVerificationEmail(req);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PutMapping("/verify")
    public ResponseEntity<BaseApiResponse<String>> completeEmailVerification(@Valid @RequestBody CompleteEmailVerificationRequest req) {
        BaseApiResponse<String> res = authenticationService.completeEmailVerification(req);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PutMapping("/phone")
    public ResponseEntity<BaseApiResponse<String>> setPhoneNumber(@Valid @RequestBody SetPhoneNumberRequest req) {
        BaseApiResponse<String> res = authenticationService.setPhoneNumber(req);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PutMapping("/update/phone")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<String>> changePhoneNumber(
            @Valid @RequestBody ChangePhoneNumberRequest req,
            @AuthenticationPrincipal ApplicationUser user
    ) {
        BaseApiResponse<String> res = authenticationService.changePhoneNumber(user.getUsername(), req);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PutMapping("/password")
    public ResponseEntity<BaseApiResponse<String>> setPassword(@Valid @RequestBody SetPasswordRequest req) {
        BaseApiResponse<String> res = authenticationService.setPassword(req);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PostMapping("/email/password-recovery/send")
    public ResponseEntity<BaseApiResponse<SendPasswordRecoveryEmailResponse>> sendPasswordRecoveryEmail(@Valid @RequestBody SendPasswordRecoveryEmailRequest req) throws EmailFailedToSentException  {
        BaseApiResponse<SendPasswordRecoveryEmailResponse> res = authenticationService.sendPasswordRecoveryEmail(req);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PostMapping("/email/password-recovery/resend")
    public ResponseEntity<BaseApiResponse<SendPasswordRecoveryEmailResponse>> resendPasswordRecoveryEmail(@Valid @RequestBody SendPasswordRecoveryEmailRequest req) throws EmailFailedToSentException  {
        BaseApiResponse<SendPasswordRecoveryEmailResponse> res = authenticationService.resendPasswordRecoveryEmail(req);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PutMapping("/recover-password")
    public ResponseEntity<BaseApiResponse<String>> recoverPassword(@Valid @RequestBody RecoverPasswordRequest req) {
        BaseApiResponse<String> res = authenticationService.recoverPassword(req);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PutMapping("/update/password")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<String>> changePassword(
            @Valid @RequestBody ChangePasswordRequest req,
            @AuthenticationPrincipal ApplicationUser user
    ) {
        BaseApiResponse<String> res = authenticationService.changePassword(user.getUsername(), req);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PostMapping("/login")
    public ResponseEntity<BaseApiResponse<AuthTokenResponse>> login(@Valid @RequestBody LoginRequest req) {
        BaseApiResponse<AuthTokenResponse> res = authenticationService.login(req);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<BaseApiResponse<AuthTokenResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest req) {
        BaseApiResponse<AuthTokenResponse> res = authenticationService.refreshToken(req);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PostMapping("/logout")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<String>> logout(@AuthenticationPrincipal ApplicationUser user) {
        BaseApiResponse<String> res = authenticationService.logout(user.getUsername());
        return ResponseEntity.status(res.getStatus()).body(res);
    }

}
