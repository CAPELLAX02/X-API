package com.x.backend.services.auth;

import com.x.backend.dto.auth.request.*;
import com.x.backend.dto.auth.response.AuthTokenResponse;
import com.x.backend.dto.auth.response.SendPasswordRecoveryEmailResponse;
import com.x.backend.dto.auth.response.SendVerificationEmailResponse;
import com.x.backend.dto.auth.response.StartRegistrationResponse;
import com.x.backend.exceptions.auth.*;
import com.x.backend.exceptions.email.EmailFailedToSentException;
import com.x.backend.exceptions.user.UserNotFoundByEmailException;
import com.x.backend.models.user.ApplicationUser;
import com.x.backend.models.user.auth.RefreshToken;
import com.x.backend.repositories.*;
import com.x.backend.security.password.PasswordEncodingConfig;
import com.x.backend.services.auth.strategies.LoginStrategy;
import com.x.backend.services.auth.util.*;
import com.x.backend.services.email.MailService;
import com.x.backend.services.token.JwtService;
import com.x.backend.services.user.UserService;
import com.x.backend.utils.api.BaseApiResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    private final ApplicationUserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UsernameGenerationService usernameGenerationService;
    private final MailService mailService;
    private final PasswordEncodingConfig passwordEncodingConfig;
    private final List<LoginStrategy> loginStrategies;
    private final JwtService jwtService;
    private final UserService userService;
    private final RoleRepository roleRepository;
    private final PrivacySettingsRepository privacySettingsRepository;
    private final PasswordHistoryRepository passwordHistoryRepository;
    private final ValidAccessTokenRepository validAccessTokenRepository;

    public AuthenticationServiceImpl(final ApplicationUserRepository userRepository,
                                     final UsernameGenerationService usernameGenerationService,
                                     final MailService mailService,
                                     final PasswordEncodingConfig passwordEncoderConfig,
                                     final List<LoginStrategy> loginStrategies,
                                     final JwtService jwtService,
                                     final RefreshTokenRepository refreshTokenRepository,
                                     final UserService userService,
                                     final RoleRepository roleRepository,
                                     final PrivacySettingsRepository privacySettingsRepository,
                                     final PasswordHistoryRepository passwordHistoryRepository,
                                     final ValidAccessTokenRepository validAccessTokenRepository
    ) {
        this.userRepository = userRepository;
        this.usernameGenerationService = usernameGenerationService;
        this.mailService = mailService;
        this.passwordEncodingConfig = passwordEncoderConfig;
        this.loginStrategies = loginStrategies;
        this.jwtService = jwtService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.privacySettingsRepository = privacySettingsRepository;
        this.passwordHistoryRepository = passwordHistoryRepository;
        this.validAccessTokenRepository = validAccessTokenRepository;
    }

    private ApplicationUser getUserByUsername(String username) {
        return userService.getUserByUsername(username);
    }

    private ApplicationUser getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundByEmailException(email));
    }

    @Override
    public BaseApiResponse<StartRegistrationResponse> startRegistration(StartRegistrationRequest req) {
        UserRegistrationManager.validateUniqueness(req.email(), req.firstName(), req.lastName(), userRepository, usernameGenerationService);

        ApplicationUser user = UserRegistrationManager.createUser(req, userRepository, roleRepository, usernameGenerationService);
        userRepository.save(user);

        UserRegistrationManager.initializePrivacySettings(user, privacySettingsRepository);

        return BaseApiResponse.success(new StartRegistrationResponse(user.getUsername()), "Registration started, username generated.");
    }

    @Override
    public BaseApiResponse<SendVerificationEmailResponse> sendVerificationEmail(SendVerificationEmailRequest req) throws EmailFailedToSentException {
        ApplicationUser user = getUserByUsername(req.username());

        if (user.isEnabled()) {
            throw new EmailAlreadyVerifiedException(user.getEmail());
        }

        String code = CodeGenerator.generateVerificationCode();
        Instant expiry = Instant.now().plusSeconds(300);

        user.setVerificationCode(code);
        user.setVerificationCodeExpiry(expiry);
        userRepository.save(user);

        EmailDispatcher.sendVerificationEmail(mailService, user.getEmail(), user.getFullName(), code);

        return BaseApiResponse.success(new SendVerificationEmailResponse(expiry), "Verification code sent via email.");
    }

    @Override
    public BaseApiResponse<SendVerificationEmailResponse> resendVerificationEmail(SendVerificationEmailRequest req) throws EmailFailedToSentException {
        ApplicationUser user = getUserByUsername(req.username());

        if (user.isEnabled()) {
            throw new EmailAlreadyVerifiedException(user.getEmail());
        }

        EmailVerificationGuard.checkThrottleLimit(user.getVerificationCodeExpiry());

        String code = CodeGenerator.generateVerificationCode();
        Instant expiry = Instant.now().plusSeconds(300);

        user.setVerificationCode(code);
        user.setVerificationCodeExpiry(expiry);
        userRepository.save(user);

        EmailDispatcher.sendVerificationEmail(mailService, user.getEmail(), user.getFullName(), code);

        SendVerificationEmailResponse res = new SendVerificationEmailResponse(expiry);
        return BaseApiResponse.success(res, "New verification code sent via email.");
    }

    @Override
    public BaseApiResponse<String> completeEmailVerification(CompleteEmailVerificationRequest req) {
        ApplicationUser user = getUserByUsername(req.username());

        if (user.isEnabled()) {
            throw new EmailAlreadyVerifiedException(user.getEmail());
        }

        EmailVerificationGuard.validateCode(user, req.verificationCode());

        user.setEnabled(true);
        user.setVerificationCode(null);
        user.setVerificationCodeExpiry(null);
        userRepository.save(user);

        return BaseApiResponse.success("Email verification completed successfully.");
    }

    @Override
    public BaseApiResponse<String> setPhoneNumber(SetPhoneNumberRequest req) {
        ApplicationUser user = getUserByUsername(req.username());

        if (userRepository.existsByPhone(req.phoneNumber())) {
            throw new PhoneNumberAlreadyInUseException(req.phoneNumber());
        }

        user.setPhone(req.phoneNumber());
        userRepository.save(user);

        return BaseApiResponse.success("Phone number set as " + req.phoneNumber() + " successfully.");
    }

    @Override
    public BaseApiResponse<String> changePhoneNumber(String username, ChangePhoneNumberRequest req) {
        ApplicationUser user = getUserByUsername(username);

        if (userRepository.existsByPhone(req.newPhoneNumber())) {
            throw new PhoneNumberAlreadyInUseException(req.newPhoneNumber());
        }

        user.setPhone(req.newPhoneNumber());
        userRepository.save(user);

        return BaseApiResponse.success("Phone number changed as " + req.newPhoneNumber() + " successfully.");
    }

    @Override
    public BaseApiResponse<String> setPassword(SetPasswordRequest req) {
        ApplicationUser user = getUserByUsername(req.username());

        if (!user.isEnabled()) {
            throw new UserIsNotEnabledException();
        }

        String encodedPassword = PasswordSecurityHandler.encodePassword(req.password(), passwordEncodingConfig);
        user.setPassword(encodedPassword);
        PasswordSecurityHandler.storePasswordHistory(user, encodedPassword, passwordHistoryRepository);
        userRepository.save(user);

        return BaseApiResponse.success("Password set successfully.");
    }

    @Override
    public BaseApiResponse<SendPasswordRecoveryEmailResponse> sendPasswordRecoveryEmail(SendPasswordRecoveryEmailRequest req) throws EmailFailedToSentException {
        ApplicationUser user = getUserByEmail(req.email());

        String code = CodeGenerator.generatePasswordRecoveryCode();
        Instant expiry = Instant.now().plusSeconds(300);

        user.setPasswordRecoveryCode(code);
        user.setPasswordRecoveryCodeExpiry(expiry);
        userRepository.save(user);

        EmailDispatcher.sendPasswordRecoveryEmail(mailService, user.getEmail(), user.getFullName(), code);

        SendPasswordRecoveryEmailResponse res = new SendPasswordRecoveryEmailResponse(expiry);
        return BaseApiResponse.success(res, "Password recovery code sent via email.");
    }

    @Override
    public BaseApiResponse<SendPasswordRecoveryEmailResponse> resendPasswordRecoveryEmail(SendPasswordRecoveryEmailRequest req) throws EmailFailedToSentException {
        ApplicationUser user = getUserByEmail(req.email());

        EmailVerificationGuard.checkThrottleLimit(user.getPasswordRecoveryCodeExpiry());

        String code = CodeGenerator.generatePasswordRecoveryCode();
        Instant expiry = Instant.now().plusSeconds(300);

        user.setPasswordRecoveryCode(code);
        user.setPasswordRecoveryCodeExpiry(expiry);
        userRepository.save(user);

        EmailDispatcher.sendPasswordRecoveryEmail(mailService, user.getEmail(), user.getFullName(), code);

        SendPasswordRecoveryEmailResponse res = new SendPasswordRecoveryEmailResponse(expiry);
        return BaseApiResponse.success(res, "New password recovery code sent via email.");
    }

    @Override
    public BaseApiResponse<String> recoverPassword(RecoverPasswordRequest req) {
        ApplicationUser user = getUserByEmail(req.email());

        EmailVerificationGuard.validateCode(user, req.passwordRecoveryCode());

        if (!req.newPassword().equals(req.newPasswordAgain())) {
            throw new PasswordDoesNotMatchException("New passwords do not match.");
        }

        PasswordSecurityHandler.checkPasswordReuse(user, req.newPassword(), passwordEncodingConfig, passwordHistoryRepository);
        String encodedPassword = PasswordSecurityHandler.encodePassword(req.newPassword(), passwordEncodingConfig);

        user.setPassword(encodedPassword);
        user.setPasswordRecoveryCode(null);
        user.setPasswordRecoveryCodeExpiry(null);
        userRepository.save(user);

        PasswordSecurityHandler.storePasswordHistory(user, encodedPassword, passwordHistoryRepository);

        return BaseApiResponse.success("Password reset successfully.");
    }

    @Override
    public BaseApiResponse<String> changePassword(String username, ChangePasswordRequest req) {
        ApplicationUser user = getUserByUsername(username);

        if (!passwordEncodingConfig.passwordEncoder().matches(req.oldPassword(), user.getPassword())) {
            throw new PasswordDoesNotMatchException("Old password incorrect.");
        }

        if (!req.newPassword().equals(req.newPasswordAgain())) {
            throw new PasswordDoesNotMatchException("New passwords do not match.");
        }

        PasswordSecurityHandler.checkPasswordReuse(user, req.newPassword(), passwordEncodingConfig, passwordHistoryRepository);
        String encoded = PasswordSecurityHandler.encodePassword(req.newPassword(), passwordEncodingConfig);
        user.setPassword(encoded); userRepository.save(user);
        PasswordSecurityHandler.storePasswordHistory(user, encoded, passwordHistoryRepository);

        return BaseApiResponse.success("Password changed successfully.");
    }

    @Override
    public BaseApiResponse<AuthTokenResponse> login(LoginRequest req) {
        if (!req.isValid()) {
            throw new InvalidLoginCredentialsException();
        }

        LoginStrategy strategy = loginStrategies.stream()
                .filter(s -> s.supports(req.getLoginKey()))
                .findFirst()
                .orElseThrow(InvalidLoginRequestKeyException::new);

        ApplicationUser user = strategy.authenticate(req);

        if (!user.isEnabled()) {
            throw new UserIsNotEnabledException();
        }

        AccessTokenRegistry.revokeAll(user, validAccessTokenRepository);
        refreshTokenRepository.deleteByUser(user);

        AuthTokenResponse res = TokenIssuanceService.generateAndStoreTokens(user, jwtService, validAccessTokenRepository, refreshTokenRepository);
        return BaseApiResponse.success(res, "Logged in successfully.");
    }

    @Override
    public BaseApiResponse<AuthTokenResponse> refreshToken(RefreshTokenRequest req) {
        RefreshToken token = refreshTokenRepository.findByToken(req.refreshToken()).orElseThrow(RefreshTokenNotFoundException::new);

        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new InvalidOrExpiredRefreshTokenException();
        }

        ApplicationUser user = token.getUser();
        AccessTokenRegistry.revokeAll(user, validAccessTokenRepository);

        AuthTokenResponse res = TokenIssuanceService.generateAndStoreTokens(user, jwtService, validAccessTokenRepository, refreshTokenRepository);
        return BaseApiResponse.success(res, "Refreshed token successfully.");
    }

    @Override
    public BaseApiResponse<String> logout(String username) {
        ApplicationUser user = getUserByUsername(username);

        refreshTokenRepository.deleteByUser(user);
        AccessTokenRegistry.revokeAll(user, validAccessTokenRepository);

        return BaseApiResponse.success("Logged out successfully.");
    }

}
