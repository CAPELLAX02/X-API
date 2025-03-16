package com.x.backend.services.auth;

import com.x.backend.dto.auth.request.*;
import com.x.backend.dto.auth.response.*;
import com.x.backend.exceptions.email.EmailFailedToSentException;
import com.x.backend.utils.api.BaseApiResponse;

public interface AuthenticationService {

    BaseApiResponse<StartRegistrationResponse> startRegistration(StartRegistrationRequest req);
    BaseApiResponse<SendVerificationEmailResponse> sendVerificationEmail(SendVerificationEmailRequest req) throws EmailFailedToSentException;
    BaseApiResponse<SendVerificationEmailResponse> resendVerificationEmail(SendVerificationEmailRequest req) throws EmailFailedToSentException;
    BaseApiResponse<String> completeEmailVerification(CompleteEmailVerificationRequest req);
    BaseApiResponse<String> setPhoneNumber(SetPhoneNumberRequest req);
    BaseApiResponse<String> changePhoneNumber(String username, ChangePhoneNumberRequest req);
    BaseApiResponse<String> setPassword(SetPasswordRequest req);
    BaseApiResponse<SendPasswordRecoveryEmailResponse> sendPasswordRecoveryEmail(SendPasswordRecoveryEmailRequest req) throws EmailFailedToSentException;
    BaseApiResponse<SendPasswordRecoveryEmailResponse> resendPasswordRecoveryEmail(SendPasswordRecoveryEmailRequest req) throws EmailFailedToSentException;
    BaseApiResponse<String> recoverPassword(RecoverPasswordRequest req);
    BaseApiResponse<String> changePassword(String username, ChangePasswordRequest req);
    BaseApiResponse<AuthTokenResponse> login(LoginRequest req);
    BaseApiResponse<AuthTokenResponse> refreshToken(RefreshTokenRequest req);
}
