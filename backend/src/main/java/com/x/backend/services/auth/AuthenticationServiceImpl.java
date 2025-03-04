package com.x.backend.services.auth;

import com.x.backend.dto.AuthTokenResponse;
import com.x.backend.dto.RegisterRequest;
import com.x.backend.dto.UserResponse;
import com.x.backend.repositories.ApplicationUserRepository;
import com.x.backend.services.email.EmailService;
import com.x.backend.utils.api.BaseApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    private final ApplicationUserRepository applicationUserRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationServiceImpl(
            ApplicationUserRepository applicationUserRepository,
            EmailService emailService,
            PasswordEncoder passwordEncoder
    ) {
        this.applicationUserRepository = applicationUserRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ResponseEntity<BaseApiResponse<UserResponse>> register(RegisterRequest registerRequest) {
        return null;
    }

    @Override
    public ResponseEntity<BaseApiResponse<String>> sendVerificationEmail(String username, String email) {
        return null;
    }

    @Override
    public ResponseEntity<BaseApiResponse<String>> resendVerificationEmail(String username, String email) {
        return null;
    }

    @Override
    public ResponseEntity<BaseApiResponse<UserResponse>> completeEmailVerification(String username, String email, String verificationCode) {
        return null;
    }

    @Override
    public ResponseEntity<BaseApiResponse<UserResponse>> setPhoneNumber(String username, String phoneNumber) {
        return null;
    }

    @Override
    public ResponseEntity<BaseApiResponse<UserResponse>> changePhoneNumber(String username, String oldPhoneNumber, String newPhoneNumber) {
        return null;
    }

    @Override
    public ResponseEntity<BaseApiResponse<UserResponse>> setPassword(String username, String password) {
        return null;
    }

    @Override
    public ResponseEntity<BaseApiResponse<String>> sendPasswordRecoveryEmail(String username, String email) {
        return null;
    }

    @Override
    public ResponseEntity<BaseApiResponse<String>> resendPasswordRecoveryEmail(String username, String email) {
        return null;
    }

    @Override
    public ResponseEntity<BaseApiResponse<UserResponse>> recoverPassword(String username, String newPassword) {
        return null;
    }

    @Override
    public ResponseEntity<BaseApiResponse<UserResponse>> changePassword(String username, String oldPassword, String newPassword) {
        return null;
    }

    @Override
    public ResponseEntity<BaseApiResponse<AuthTokenResponse>> login(String username, String password) {
        return null;
    }

    @Override
    public ResponseEntity<BaseApiResponse<AuthTokenResponse>> refreshToken(String refreshToken) {
        return null;
    }
}
