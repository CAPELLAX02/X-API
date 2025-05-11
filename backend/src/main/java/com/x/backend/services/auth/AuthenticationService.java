package com.x.backend.services.auth;

import com.x.backend.dto.auth.request.*;
import com.x.backend.dto.auth.response.*;
import com.x.backend.exceptions.auth.*;
import com.x.backend.exceptions.common.TooManyRequestsException;
import com.x.backend.exceptions.email.EmailFailedToSentException;
import com.x.backend.exceptions.user.UserNotFoundByEmailException;
import com.x.backend.utils.api.BaseApiResponse;

/**
 * AuthenticationService defines the contract for all authentication-related workflows
 * within the application. It encapsulates user registration, login, email verification,
 * password setup and recovery, token lifecycle management, and session invalidation.
 * <p>
 * This service plays a central role in securing the platform by enforcing:
 * <ul>
 *     <li>Email-based identity validation</li>
 *     <li>Phone number uniqueness and assignment</li>
 *     <li>Password history tracking and reuse prevention</li>
 *     <li>JWT access and refresh token lifecycle with session-level invalidation</li>
 *     <li>Support for login via multiple identifiers (username, email, phone)</li>
 * </ul>
 * <p>
 * AuthenticationService is stateless and should be used in conjunction with a token-based
 * authentication system such as JWT. All operations are idempotent where applicable,
 * and rely on rate-limited flows to prevent abuse.
 *
 * <p>
 * Example onboarding flow:
 * <ol>
 *     <li>{@code startRegistration}</li>
 *     <li>{@code setPhoneNumber}</li>
 *     <li>{@code sendVerificationEmail} → {@code completeEmailVerification}</li>
 *     <li>{@code setPassword}</li>
 *     <li>{@code login}</li>
 * </ol>
 *
 * Implemented by {@link com.x.backend.services.auth.AuthenticationServiceImpl}.
 */
public interface AuthenticationService {

    /**
     * Begins the user registration process by saving the basic user profile
     * (name, email, date of birth) and generating a unique system username.
     * The account remains disabled until email verification and password setup are completed.
     *
     * @param req contains first name, last name, email, and date of birth
     * @return a generated system username for the user
     * @throws EmailAlreadyInUseException if the email is already registered
     */
    BaseApiResponse<StartRegistrationResponse> startRegistration(StartRegistrationRequest req);

    /**
     * Sends a verification code to the email address of the given username.
     * This code is valid for 5 minutes.
     *
     * @param req contains the username to send verification code to
     * @return the expiry timestamp of the verification code
     * @throws EmailAlreadyVerifiedException if the user's account is already verified
     * @throws EmailFailedToSentException if the email could not be sent
     */
    BaseApiResponse<SendVerificationEmailResponse> sendVerificationEmail(SendVerificationEmailRequest req) throws EmailFailedToSentException;

    /**
     * Sends a new email verification code to the user if the last one is expired or nearly expired.
     * Enforces a cooldown period of 60 seconds to prevent abuse.
     *
     * @param req contains the username to resend verification code to
     * @return the new expiry timestamp
     * @throws TooManyRequestsException if the request is made too soon after the previous one
     * @throws EmailFailedToSentException if the email could not be sent
     */
    BaseApiResponse<SendVerificationEmailResponse> resendVerificationEmail(SendVerificationEmailRequest req) throws EmailFailedToSentException;

    /**
     * Validates the provided verification code and enables the user's account upon success.
     *
     * @param req contains username and the verification code
     * @return success message if verification is complete
     * @throws InvalidVerificationCodeException if the code is incorrect
     * @throws ExpiredVerificationCodeException if the code has expired
     */
    BaseApiResponse<String> completeEmailVerification(CompleteEmailVerificationRequest req);

    /**
     * Assigns a phone number to the user during onboarding.
     * Phone numbers must be unique across the platform.
     *
     * @param req contains username and phone number
     * @return confirmation message
     * @throws PhoneNumberAlreadyInUseException if the phone is already linked to another user
     */
    BaseApiResponse<String> setPhoneNumber(SetPhoneNumberRequest req);

    /**
     * Allows an authenticated user to update their phone number.
     *
     * @param username the username of the user performing the change
     * @param req contains the new phone number
     * @return a message indicating the change was successful
     * @throws PhoneNumberAlreadyInUseException if the new number is already taken
     */
    BaseApiResponse<String> changePhoneNumber(String username, ChangePhoneNumberRequest req);

    /**
     * Sets the user's password for the first time after verification.
     * This is part of the onboarding process and can only be done once.
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
     * @param req contains the registered email
     * @return expiry time of the recovery code
     * @throws UserNotFoundByEmailException if no user is found with the given email
     * @throws EmailFailedToSentException if the email fails to send
     */
    BaseApiResponse<SendPasswordRecoveryEmailResponse> sendPasswordRecoveryEmail(SendPasswordRecoveryEmailRequest req) throws EmailFailedToSentException;

    /**
     * Resends the password recovery code if a recent one is not already active.
     * Enforces a cooldown window.
     *
     * @param req contains the email address
     * @return new code expiry time
     * @throws TooManyRequestsException if requested too soon
     * @throws EmailFailedToSentException if sending fails
     */
    BaseApiResponse<SendPasswordRecoveryEmailResponse> resendPasswordRecoveryEmail(SendPasswordRecoveryEmailRequest req) throws EmailFailedToSentException;

    /**
     * Validates the password recovery code and resets the user's password.
     * Also checks for reuse of any of the last 3 passwords.
     *
     * @param req contains email, code, and new password (entered twice)
     * @return confirmation message
     * @throws InvalidPasswordRecoveryCodeException if code is invalid
     * @throws ExpiredPasswordRecoveryCodeException if code has expired
     * @throws PasswordDoesNotMatchException if new passwords do not match
     * @throws PasswordReusedException if new password was used recently
     */
    BaseApiResponse<String> recoverPassword(RecoverPasswordRequest req);

    /**
     * Changes the user's password after validating the current one.
     * Prevents reuse of recently used passwords.
     *
     * @param username the user initiating the change
     * @param req contains current password and new password (entered twice)
     * @return confirmation message
     * @throws PasswordDoesNotMatchException if old password is wrong or new ones don't match
     * @throws PasswordReusedException if password was used recently
     */
    BaseApiResponse<String> changePassword(String username, ChangePasswordRequest req);

    /**
     * Authenticates the user based on their login credentials (username/email/phone).
     * Issues a new access token and refresh token on success.
     *
     * @param req contains login identifier and password
     * @return a valid JWT token pair and expiration info
     * @throws InvalidLoginCredentialsException if validation fails
     * @throws UserIsNotEnabledException if email verification is not complete
     */
    BaseApiResponse<AuthTokenResponse> login(LoginRequest req);

    /**
     * Refreshes the user's access token using a valid refresh token.
     * Invalidates the previous access token session.
     *
     * @param req contains the refresh token
     * @return new JWT token pair
     * @throws RefreshTokenNotFoundException if the token does not exist
     * @throws InvalidOrExpiredRefreshTokenException if the token is expired or invalid
     */
    BaseApiResponse<AuthTokenResponse> refreshToken(RefreshTokenRequest req);

    /**
     * Logs the user out by deleting their refresh token and invalidating current sessions.
     *
     * @param username of the user to logout
     * @return logout confirmation
     */
    BaseApiResponse<String> logout(String username);
}
