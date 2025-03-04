package com.x.backend.services.auth;

import com.x.backend.dto.AuthTokenResponse;
import com.x.backend.dto.RegisterRequest;
import com.x.backend.dto.UserResponse;
import com.x.backend.utils.api.BaseApiResponse;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {

    ResponseEntity<BaseApiResponse<UserResponse>> register(RegisterRequest registerRequest);
    ResponseEntity<BaseApiResponse<String>> sendVerificationEmail(String username, String email);
    ResponseEntity<BaseApiResponse<String>> resendVerificationEmail(String username, String email);
    ResponseEntity<BaseApiResponse<UserResponse>> completeEmailVerification(String username, String email, String verificationCode);
    ResponseEntity<BaseApiResponse<UserResponse>> setPhoneNumber(String username, String phoneNumber);
    ResponseEntity<BaseApiResponse<UserResponse>> changePhoneNumber(String username, String oldPhoneNumber, String newPhoneNumber);
    ResponseEntity<BaseApiResponse<UserResponse>> setPassword(String username, String password);
    ResponseEntity<BaseApiResponse<String>> sendPasswordRecoveryEmail(String username, String email);
    ResponseEntity<BaseApiResponse<String>> resendPasswordRecoveryEmail(String username, String email);
    ResponseEntity<BaseApiResponse<UserResponse>> recoverPassword(String username, String newPassword);
    ResponseEntity<BaseApiResponse<UserResponse>> changePassword(String username, String oldPassword, String newPassword);
    ResponseEntity<BaseApiResponse<AuthTokenResponse>> login(String username, String password);
    ResponseEntity<BaseApiResponse<AuthTokenResponse>> refreshToken(String refreshToken);

}
