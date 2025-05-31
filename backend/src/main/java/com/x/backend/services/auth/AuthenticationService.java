package com.x.backend.services.auth;

import com.x.backend.dto.auth.request.*;
import com.x.backend.dto.auth.response.*;
import com.x.backend.exceptions.auth.*;
import com.x.backend.exceptions.common.TooManyRequestsException;
import com.x.backend.exceptions.email.EmailFailedToSentException;
import com.x.backend.exceptions.user.UserNotFoundByEmailException;
import com.x.backend.utils.api.BaseApiResponse;

/**
 * AuthenticationService provides all necessary user authentication operations
 * including registration, email verification, password setup and recovery,
 * login, logout, and token management. It supports a multi-step registration
 * flow and enforces account security through features like password history tracking
 * and email-based verification codes.
 *
 * <p>Typical user registration flow:
 * <ol>
 *     <li>{@link #startRegistration(StartRegistrationRequest)}</li>
 *     <li>{@link #setPhoneNumber(SetPhoneNumberRequest)}</li>
 *     <li>{@link #sendVerificationEmail(SendVerificationEmailRequest)}</li>
 *     <li>{@link #completeEmailVerification(CompleteEmailVerificationRequest)}</li>
 *     <li>{@link #setPassword(SetPasswordRequest)}</li>
 *     <li>{@link #login(LoginRequest)}</li>
 * </ol>
 *
 * <p>Each method in this interface is designed to return a {@link BaseApiResponse}
 * to provide consistent API behavior with status and payload wrapping.
 */
public interface AuthenticationService {

    /**
     * Starts the registration process by creating a disabled user with a unique username and default role.
     *
     * @param req the registration request containing:
     *            - {@code firstName}: user's first name
     *            - {@code lastName}: user's last name
     *            - {@code email}: user's email address
     *            - {@code dateOfBirth}: user's birthdate
     * @return the generated username in the response
     * @throws EmailAlreadyInUseException if a user with the given email already exists
     * @throws UsernameAlreadyInUseException not expected to be thrown, would occur if the system had generated the exact same username upon a successful registration
     */
    BaseApiResponse<StartRegistrationResponse> startRegistration(StartRegistrationRequest req);

    /**
     * Sends a verification code to the user's registered email address.
     * This code is valid for 5 minutes and is required to complete the email verification step.
     *
     * @param req contains the {@code username} to send the code to
     * @return expiry timestamp of the verification code
     * @throws EmailAlreadyVerifiedException if the user is already verified
     * @throws EmailFailedToSentException if email delivery fails
     */
    BaseApiResponse<SendVerificationEmailResponse> sendVerificationEmail(SendVerificationEmailRequest req) throws EmailFailedToSentException;

    /**
     * Resends the email verification code if the last request is at least 1 minute old.
     *
     * @param req contains the {@code username}
     * @return expiry timestamp of the new code
     * @throws EmailAlreadyVerifiedException if the user is already verified
     * @throws TooManyRequestsException if the resend interval has not passed
     * @throws EmailFailedToSentException if email delivery fails
     */
    BaseApiResponse<SendVerificationEmailResponse> resendVerificationEmail(SendVerificationEmailRequest req) throws EmailFailedToSentException;

    /**
     * Completes the email verification by validating the code.
     *
     * @param req contains:
     *            - {@code username}: target user
     *            - {@code verificationCode}: code sent via email
     * @return success message
     * @throws EmailAlreadyVerifiedException if already verified
     * @throws InvalidVerificationCodeException if the code does not match
     * @throws ExpiredVerificationCodeException if the code has expired
     */
    BaseApiResponse<String> completeEmailVerification(CompleteEmailVerificationRequest req);

    /**
     * Assigns a phone number to the user during onboarding.
     * Phone numbers must be unique across the platform.
     *
     * @param req contains:
     *            - {@code username}
     *            - {@code phoneNumber}
     * @return confirmation message
     * @throws PhoneNumberAlreadyInUseException if the number is already used by another user
     */
    BaseApiResponse<String> setPhoneNumber(SetPhoneNumberRequest req);

    /**
     * Allows an authenticated user to update their phone number.
     *
     * @param username current user
     * @param req contains:
     *            - {@code newPhoneNumber}
     * @return confirmation message
     * @throws PhoneNumberAlreadyInUseException if the new number is already taken
     */
    BaseApiResponse<String> changePhoneNumber(String username, ChangePhoneNumberRequest req);

    /**
     * Sets the user's password for the first time after verification, as a part of onboarding process.
     *
     * @param req contains username and new password
     * @return success message
     * @throws UserIsNotEnabledException if the user's account is not yet verified
     */
    BaseApiResponse<String> setPassword(SetPasswordRequest req);

    /**
     * Sends a one-time password recovery code to the user's email.
     * This code expires in 5 minutes.
     *
     * @param req contains:
     *            - {@code email}
     * @return code expiry timestamp
     * @throws UserNotFoundByEmailException if the email is not associated with any account
     * @throws EmailFailedToSentException if the email fails to send
     */
    BaseApiResponse<SendPasswordRecoveryEmailResponse> sendPasswordRecoveryEmail(SendPasswordRecoveryEmailRequest req) throws EmailFailedToSentException;

    /**
     * Resends the password recovery code if a recent one is not already active.
     * Enforces a cooldown window.
     *
     * @param req contains:
     *            - {@code email}
     * @return new code's expiry time
     * @throws UserNotFoundByEmailException if email is not found
     * @throws TooManyRequestsException if 1 minute has not passed since the last code
     * @throws EmailFailedToSentException if email delivery fails
     */
    BaseApiResponse<SendPasswordRecoveryEmailResponse> resendPasswordRecoveryEmail(SendPasswordRecoveryEmailRequest req) throws EmailFailedToSentException;

    /**
     * Validates the password recovery code and resets the user's password.
     * Also checks for reuse of the last 3 passwords.
     *
     * @param req contains:
     *            - {@code email}
     *            - {@code passwordRecoveryCode}
     *            - {@code newPassword}
     *            - {@code newPasswordAgain}
     * @return success message
     * @throws InvalidPasswordRecoveryCodeException if code is incorrect
     * @throws ExpiredPasswordRecoveryCodeException if the code is expired
     * @throws PasswordDoesNotMatchException if new passwords do not match
     * @throws PasswordReusedException if new password was used recently
     */
    BaseApiResponse<String> recoverPassword(RecoverPasswordRequest req);

    /**
     * Changes the user's password after validating the current one.
     * Prevents reuse of recently used passwords.
     *
     * @param username current username
     * @param req contains:
     *            - {@code oldPassword}
     *            - {@code newPassword}
     *            - {@code newPasswordAgain}
     * @return success message
     * @throws PasswordDoesNotMatchException if the old password is wrong or new passwords mismatch
     * @throws PasswordReusedException if new password was recently used
     */
    BaseApiResponse<String> changePassword(String username, ChangePasswordRequest req);

    /**
     * Authenticates a user using one of multiple login strategies (email, phone, or username).
     * Invalidates the previous access token using the JWT blacklist, for further security measure.
     *
     * @param req contains:
     *            - login identifier (email, phone, or username)
     *            - password
     * @return JWT access & refresh tokens with expiration time
     * @throws InvalidLoginCredentialsException if request is malformed
     * @throws InvalidLoginRequestKeyException if no matching strategy exists
     * @throws UserIsNotEnabledException if the user is not email verified
     */
    BaseApiResponse<AuthTokenResponse> login(LoginRequest req);

    /**
     * Refreshes the user's access token using a valid refresh token.
     * Invalidates the previous access token manipulating the JWT blacklist.
     *
     * @param req contains:
     *            - {@code refreshToken}
     * @return new access & refresh tokens with expiration
     * @throws RefreshTokenNotFoundException if token is not in storage
     * @throws InvalidOrExpiredRefreshTokenException if token has expired
     */
    BaseApiResponse<AuthTokenResponse> refreshToken(RefreshTokenRequest req);

    /**
     * Logs the user out by deleting their refresh token and invalidating access token.
     *
     * @param username of the user to logout
     * @return logout confirmation
     */
    BaseApiResponse<String> logout(String username);
}
