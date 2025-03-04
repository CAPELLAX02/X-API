package com.x.backend.services.auth;

import com.x.backend.dto.*;
import com.x.backend.utils.api.BaseApiResponse;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {

    ResponseEntity<BaseApiResponse<UserResponse>> register(RegisterRequest req);
    ResponseEntity<BaseApiResponse<String>> sendVerificationEmail(SendVerificationEmailRequest req);
    ResponseEntity<BaseApiResponse<String>> resendVerificationEmail(SendVerificationEmailRequest req);
    ResponseEntity<BaseApiResponse<UserResponse>> completeEmailVerification(CompleteEmailVerificationRequest req);
    ResponseEntity<BaseApiResponse<UserResponse>> setPhoneNumber(SetPhoneNumberRequest req);
    ResponseEntity<BaseApiResponse<UserResponse>> changePhoneNumber(String username, ChangePhoneNumberRequest req);
    ResponseEntity<BaseApiResponse<UserResponse>> setPassword(SetPasswordRequest req);
    ResponseEntity<BaseApiResponse<String>> sendPasswordRecoveryEmail(SendPasswordRecoveryEmailRequest req);
    ResponseEntity<BaseApiResponse<String>> resendPasswordRecoveryEmail(SendPasswordRecoveryEmailRequest req);
    ResponseEntity<BaseApiResponse<UserResponse>> recoverPassword(RecoverPasswordRequest req);
    ResponseEntity<BaseApiResponse<UserResponse>> changePassword(String username, ChangePasswordRequest req);
    ResponseEntity<BaseApiResponse<AuthTokenResponse>> login(LoginRequest req);
    ResponseEntity<BaseApiResponse<AuthTokenResponse>> refreshToken(RefreshTokenRequest req);

}
