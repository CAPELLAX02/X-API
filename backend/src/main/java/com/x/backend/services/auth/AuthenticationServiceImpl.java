package com.x.backend.services.auth;

import com.x.backend.dto.*;
import com.x.backend.repositories.ApplicationUserRepository;
import com.x.backend.services.auth.strategies.LoginStrategy;
import com.x.backend.services.email.MailService;
import com.x.backend.utils.api.BaseApiResponse;
import com.x.backend.utils.common.UsernameGenerationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    private final ApplicationUserRepository applicationUserRepository;
    private final UsernameGenerationService usernameGenerationService;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final List<LoginStrategy> loginStrategies;
    private final

    public AuthenticationServiceImpl(
            ApplicationUserRepository applicationUserRepository,
            UsernameGenerationService usernameGenerationService,
            MailService mailService,
            PasswordEncoder passwordEncoder
    ) {
        this.applicationUserRepository = applicationUserRepository;
        this.usernameGenerationService = usernameGenerationService;
        this.mailService = mailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ResponseEntity<BaseApiResponse<UserResponse>> register(RegisterRequest req) {
        return null;
    }

    @Override
    public ResponseEntity<BaseApiResponse<String>> sendVerificationEmail(SendVerificationEmailRequest req) {
        return null;
    }

    @Override
    public ResponseEntity<BaseApiResponse<String>> resendVerificationEmail(SendVerificationEmailRequest req) {
        return null;
    }

    @Override
    public ResponseEntity<BaseApiResponse<UserResponse>> completeEmailVerification(CompleteEmailVerificationRequest req) {
        return null;
    }

    @Override
    public ResponseEntity<BaseApiResponse<UserResponse>> setPhoneNumber(SetPhoneNumberRequest req) {
        return null;
    }

    @Override
    public ResponseEntity<BaseApiResponse<UserResponse>> changePhoneNumber(String username, ChangePhoneNumberRequest req) {
        return null;
    }

    @Override
    public ResponseEntity<BaseApiResponse<UserResponse>> setPassword(SetPasswordRequest req) {
        return null;
    }

    @Override
    public ResponseEntity<BaseApiResponse<String>> sendPasswordRecoveryEmail(SendPasswordRecoveryEmailRequest req) {
        return null;
    }

    @Override
    public ResponseEntity<BaseApiResponse<String>> resendPasswordRecoveryEmail(SendPasswordRecoveryEmailRequest req) {
        return null;
    }

    @Override
    public ResponseEntity<BaseApiResponse<UserResponse>> recoverPassword(RecoverPasswordRequest req) {
        return null;
    }

    @Override
    public ResponseEntity<BaseApiResponse<UserResponse>> changePassword(String username, ChangePasswordRequest req) {
        return null;
    }

    @Override
    public ResponseEntity<BaseApiResponse<AuthTokenResponse>> login(LoginRequest req) {

    }

    @Override
    public ResponseEntity<BaseApiResponse<AuthTokenResponse>> refreshToken(RefreshTokenRequest req) {
        return null;
    }

}
