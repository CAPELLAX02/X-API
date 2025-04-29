package com.x.backend.services.auth;

import com.x.backend.models.entities.*;
import com.x.backend.models.enums.PrivacyLevel;
import com.x.backend.repositories.*;
import com.x.backend.security.PasswordEncodingConfig;
import com.x.backend.dto.auth.request.*;
import com.x.backend.dto.auth.response.AuthTokenResponse;
import com.x.backend.dto.auth.response.SendPasswordRecoveryEmailResponse;
import com.x.backend.dto.auth.response.SendVerificationEmailResponse;
import com.x.backend.dto.auth.response.StartRegistrationResponse;
import com.x.backend.exceptions.auth.*;
import com.x.backend.exceptions.common.TooManyRequestsException;
import com.x.backend.exceptions.email.EmailFailedToSentException;
import com.x.backend.exceptions.user.UserNotFoundByEmailException;
import com.x.backend.models.enums.RoleType;
import com.x.backend.services.auth.strategies.LoginStrategy;
import com.x.backend.services.email.MailService;
import com.x.backend.services.token.JwtService;
import com.x.backend.services.user.UserService;
import com.x.backend.utils.api.BaseApiResponse;
import com.x.backend.utils.auth.CodeGenerator;
import com.x.backend.utils.auth.UsernameGenerationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    /**
     * Initial registration process will be as follows:
     *      1. register
     *      2. setPhoneNumber
     *      3. sendEmailVerification
     *      4. completeEmailVerification
     *      5. setPassword
     *      6. login
     */

    // TODO: Trace the last 3 passwords of the user as in banking mobile applications.

    private final ApplicationUserRepository applicationUserRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UsernameGenerationService usernameGenerationService;
    private final MailService mailService;
    private final PasswordEncodingConfig passwordEncodingConfig;
    private final List<LoginStrategy> loginStrategies;
    private final JwtService jwtService;
    private final UserService userService;
    private final CodeGenerator codeGenerator;
    private final RoleRepository roleRepository;
    private final PrivacySettingsRepository privacySettingsRepository;
    private final PasswordHistoryRepository passwordHistoryRepository;

    public AuthenticationServiceImpl(ApplicationUserRepository applicationUserRepository,
                                     UsernameGenerationService usernameGenerationService,
                                     MailService mailService,
                                     PasswordEncodingConfig passwordEncoderConfig,
                                     List<LoginStrategy> loginStrategies,
                                     JwtService jwtService,
                                     RefreshTokenRepository refreshTokenRepository,
                                     UserService userService,
                                     CodeGenerator codeGenerator,
                                     RoleRepository roleRepository,
                                     PrivacySettingsRepository privacySettingsRepository,
                                     PasswordHistoryRepository passwordHistoryRepository
    ) {
        this.applicationUserRepository = applicationUserRepository;
        this.usernameGenerationService = usernameGenerationService;
        this.mailService = mailService;
        this.passwordEncodingConfig = passwordEncoderConfig;
        this.loginStrategies = loginStrategies;
        this.jwtService = jwtService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userService = userService;
        this.codeGenerator = codeGenerator;
        this.roleRepository = roleRepository;
        this.privacySettingsRepository = privacySettingsRepository;
        this.passwordHistoryRepository = passwordHistoryRepository;
    }

    private ApplicationUser getUserByUsername(String username) {
        return userService.getUserByUsername(username);
    }

    @Override
    public BaseApiResponse<StartRegistrationResponse> startRegistration(StartRegistrationRequest req) {
        if (applicationUserRepository.existsByEmail(req.email())) {
            throw new EmailAlreadyInUseException(req.email());
        }

        String generatedUsername = usernameGenerationService.generateUniqueUsername(req.firstName(), req.lastName());

        if (applicationUserRepository.existsByUsername(generatedUsername)) {
            throw new UsernameAlreadyInUseException();
        }

        ApplicationUser user = new ApplicationUser();
        user.setFirstName(req.firstName());
        user.setLastName(req.lastName());
        user.setEmail(req.email());
        user.setDateOfBirth(req.dateOfBirth());
        user.setUsername(generatedUsername);
        user.setEnabled(false);

        Role userRole = roleRepository.findByAuthority(RoleType.ROLE_USER)
                .orElseGet(() ->  {
                    Role newRole = new Role();
                    newRole.setAuthority(RoleType.ROLE_USER);
                    return roleRepository.save(newRole);
                });

        user.setAuthorities(Collections.singleton(userRole));

        applicationUserRepository.save(user);

        // Initialize the privacy setting of the user with default settings
        PrivacySettings privacySettings = new PrivacySettings();
        privacySettings.setUser(user);
        privacySettings.setMessagePrivacy(PrivacyLevel.EVERYONE);
        privacySettings.setMentionPrivacy(PrivacyLevel.EVERYONE);
        privacySettings.setPostVisibility(PrivacyLevel.EVERYONE);
        privacySettingsRepository.save(privacySettings);

        return BaseApiResponse.success(
                new StartRegistrationResponse(user.getUsername()),
                "Registration started, username generated."
        );
    }

    @Override
    public BaseApiResponse<SendVerificationEmailResponse> sendVerificationEmail(SendVerificationEmailRequest req) throws EmailFailedToSentException {
        ApplicationUser user = getUserByUsername(req.username());
        if (user.isEnabled()) {
            throw new EmailAlreadyVerifiedException(user.getEmail());
        }

        String verificationCode = codeGenerator.generateVerificationCode();
        Instant expiryTime = Instant.now().plusSeconds(300);

        user.setVerificationCode(verificationCode);
        user.setVerificationCodeExpiry(expiryTime);
        applicationUserRepository.save(user);

        mailService.sendVerificationCodeViaEmail(user.getEmail(), user.getFirstName() + " " + user.getLastName(), verificationCode);

        return BaseApiResponse.success(
                new SendVerificationEmailResponse(expiryTime),
                "Verification code sent via email."
        );

    }

    @Override
    public BaseApiResponse<SendVerificationEmailResponse> resendVerificationEmail(SendVerificationEmailRequest req) throws EmailFailedToSentException {
        ApplicationUser user = getUserByUsername(req.username());
        if (user.isEnabled()) {
            throw new EmailAlreadyVerifiedException(user.getEmail());
        }

        Instant now = Instant.now();
        Instant verificationExpiry = user.getVerificationCodeExpiry();

        if (verificationExpiry != null && verificationExpiry.minusSeconds(240).isAfter(now)) {
            throw new TooManyRequestsException("Please wait for one minute before requesting a new verification code.");
        }

        String verificationCode = codeGenerator.generateVerificationCode();
        Instant expiryTime = now.plusSeconds(300);

        user.setVerificationCode(verificationCode);
        user.setVerificationCodeExpiry(expiryTime);
        applicationUserRepository.save(user);

        mailService.sendVerificationCodeViaEmail(user.getEmail(), user.getFirstName() + " " + user.getLastName(), verificationCode);

        return BaseApiResponse.success(
                new SendVerificationEmailResponse(expiryTime),
                "New verification code sent via email."
        );
    }

    @Override
    public BaseApiResponse<String> completeEmailVerification(CompleteEmailVerificationRequest req) {
        ApplicationUser user = getUserByUsername(req.username());

        if (user.isEnabled()) {
            throw new EmailAlreadyVerifiedException(user.getEmail());
        }

        Instant now = Instant.now();

        if (user.getVerificationCode() == null || !user.getVerificationCode().equals(req.verificationCode())) {
            throw new InvalidVerificationCodeException("Invalid or incorrect verification code.");
        }

        if (user.getVerificationCodeExpiry() == null || user.getVerificationCodeExpiry().isBefore(now)) {
            throw new ExpiredVerificationCodeException("Verification code has expired. Please request a new one.");
        }

        user.setEnabled(true);
        user.setVerificationCode(null);
        user.setVerificationCodeExpiry(null);

        applicationUserRepository.save(user);

        return BaseApiResponse.success("Email verification completed successfully.");
    }

    @Override
    public BaseApiResponse<String> setPhoneNumber(SetPhoneNumberRequest req) {
        String reqPhoneNumber = req.phoneNumber();
        if (applicationUserRepository.existsByPhone(reqPhoneNumber)) {
            throw new PhoneNumberAlreadyInUseException(reqPhoneNumber);
        }
        ApplicationUser user = getUserByUsername(req.username());
        user.setPhone(reqPhoneNumber);
        applicationUserRepository.save(user);
        return BaseApiResponse.success("Phone number set as " + reqPhoneNumber + " successfully.");
    }

    @Override
    public BaseApiResponse<String> changePhoneNumber(String username, ChangePhoneNumberRequest req) {
        ApplicationUser user = getUserByUsername(username);

        String oldPhoneNumber = user.getPhone();
        String newPhoneNumber = req.newPhoneNumber();

        if (applicationUserRepository.existsByPhone(newPhoneNumber)) {
            throw new PhoneNumberAlreadyInUseException(newPhoneNumber);
        }

        user.setPhone(req.newPhoneNumber());
        applicationUserRepository.save(user);

        return BaseApiResponse.success("Phone number changed from " + oldPhoneNumber + " to " + newPhoneNumber + ".");
    }

    @Override
    public BaseApiResponse<String> setPassword(SetPasswordRequest req) {
        ApplicationUser user = getUserByUsername(req.username());
        if (!user.isEnabled()) {
            throw new UserIsNotEnabledException();
        }
        user.setPassword(passwordEncodingConfig.passwordEncoder().encode(req.password()));
        applicationUserRepository.save(user);
        return BaseApiResponse.success("User password set successfully.");
    }

    @Override
    public BaseApiResponse<SendPasswordRecoveryEmailResponse> sendPasswordRecoveryEmail(SendPasswordRecoveryEmailRequest req) throws EmailFailedToSentException {
        ApplicationUser user = applicationUserRepository.findByEmail(req.email())
                .orElseThrow(() -> new UserNotFoundByEmailException(req.email()));

        String passwordRecoveryCode = codeGenerator.generatePasswordRecoveryCode();
        Instant passwordRecoveryExpiry = Instant.now().plusSeconds(300); // 5 minutes

        user.setPasswordRecoveryCode(passwordRecoveryCode);
        user.setPasswordRecoveryCodeExpiry(passwordRecoveryExpiry);
        applicationUserRepository.save(user);

        mailService.sendPasswordRecoveryCodeViaEmail(
                user.getEmail(),
                user.getFirstName() + " " + user.getLastName(),
                passwordRecoveryCode
        );

        return BaseApiResponse.success(
                new SendPasswordRecoveryEmailResponse(passwordRecoveryExpiry),
                "Password recovery code sent via email."
        );
    }

    @Override
    public BaseApiResponse<SendPasswordRecoveryEmailResponse> resendPasswordRecoveryEmail(SendPasswordRecoveryEmailRequest req) throws EmailFailedToSentException {
        ApplicationUser user = applicationUserRepository.findByEmail(req.email())
                .orElseThrow(() -> new UserNotFoundByEmailException(req.email()));

        Instant now = Instant.now();
        Instant passwordRecoveryExpiry = user.getPasswordRecoveryCodeExpiry();

        if (passwordRecoveryExpiry != null && passwordRecoveryExpiry.minusSeconds(240).isAfter(now)) {
            throw new TooManyRequestsException("You can request a new password recovery code after one minute.");
        }

        String passwordRecoveryCode = codeGenerator.generatePasswordRecoveryCode();
        user.setPasswordRecoveryCode(passwordRecoveryCode);
        user.setPasswordRecoveryCodeExpiry(passwordRecoveryExpiry);
        applicationUserRepository.save(user);

        mailService.sendPasswordRecoveryCodeViaEmail(
                user.getEmail(),
                user.getFirstName() + " " + user.getLastName(),
                passwordRecoveryCode
        );

        return BaseApiResponse.success(
                new SendPasswordRecoveryEmailResponse(passwordRecoveryExpiry),
                "New password recovery code sent via email."
        );
    }

    @Override
    public BaseApiResponse<String> recoverPassword(RecoverPasswordRequest req) {
        ApplicationUser user = applicationUserRepository.findByEmail(req.email())
                .orElseThrow(() -> new UserNotFoundByEmailException(req.email()));

        if (user.getPasswordRecoveryCode() == null || !user.getPasswordRecoveryCode().equals(req.passwordRecoveryCode())) {
            throw new InvalidPasswordRecoveryCodeException("Invalid or incorrect password recovery code.");
        }

        if (user.getPasswordRecoveryCodeExpiry() == null || user.getPasswordRecoveryCodeExpiry().isBefore(Instant.now())) {
            throw new ExpiredPasswordRecoveryCodeException("Password recovery code has expired. Please request a new one.");
        }

        if (!Objects.equals(req.newPassword(), req.newPasswordAgain())) {
            throw new PasswordDoesNotMatchException("New passwords do not match.");
        }

        checkPasswordReuse(user, req.newPassword());

        String encodedNewPassword = passwordEncodingConfig.passwordEncoder().encode(req.newPassword());
        user.setPassword(encodedNewPassword);
        user.setPasswordRecoveryCode(null);
        user.setPasswordRecoveryCodeExpiry(null);
        applicationUserRepository.save(user);

        savePasswordHistory(user, encodedNewPassword);

        return BaseApiResponse.success("Password successfully reset.");
    }

    private void checkPasswordReuse(ApplicationUser user, String rawNewPassword) {
        List<PasswordHistory> lastPasswords = passwordHistoryRepository.findTop3ByUserOrderByChangedAtDesc(user);
        for (PasswordHistory passwordHistory : lastPasswords) {
            if (passwordEncodingConfig.passwordEncoder().matches(rawNewPassword, passwordHistory.getPassword())) {
                throw new PasswordReusedException("New password cannot be the same as any of the last 3 passwords.");
            }
        }
    }

    private void savePasswordHistory(ApplicationUser user, String encodedPassword) {
        PasswordHistory passwordHistory = new PasswordHistory(user, encodedPassword, Instant.now());
        passwordHistoryRepository.save(passwordHistory);
    }

    @Override
    public BaseApiResponse<String> changePassword(String username, ChangePasswordRequest req) {
        ApplicationUser user = getUserByUsername(username);

        if (!passwordEncodingConfig.passwordEncoder().matches(req.oldPassword(), user.getPassword())) {
            throw new PasswordDoesNotMatchException("Old password is incorrect.");
        }

        if (!req.newPassword().equals(req.newPasswordAgain())) {
            throw new PasswordDoesNotMatchException("New passwords do not match.");
        }

        checkPasswordReuse(user, req.newPassword());

        String encodedNewPassword = passwordEncodingConfig.passwordEncoder().encode(req.newPassword());
        user.setPassword(encodedNewPassword);
        applicationUserRepository.save(user);

        savePasswordHistory(user, encodedNewPassword);

        return BaseApiResponse.success("Password successfully changed.");
    }

    @Override
    public BaseApiResponse<AuthTokenResponse> login(LoginRequest req) {
        if (!req.isValid()) {
            throw new InvalidLoginCredentialsException();
        }
        String loginKey = req.getLoginKey();
        LoginStrategy loginStrategy = loginStrategies.stream()
                .filter(s -> s.supports(loginKey))
                .findFirst()
                .orElseThrow(InvalidLoginRequestKeyException::new);
        ApplicationUser user = loginStrategy.authenticate(req);
        if (!user.isEnabled()) {
            throw new UserIsNotEnabledException();
        }
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        Long expiresIn = jwtService.getExpirationFromToken(accessToken);
        refreshTokenRepository.deleteByUser(user);
        RefreshToken newRefreshToken = new RefreshToken(
                refreshToken,
                user,
                Instant.now().plusSeconds(604800)
        );
        refreshTokenRepository.save(newRefreshToken);
        return BaseApiResponse.success(
                new AuthTokenResponse(accessToken, refreshToken, expiresIn),
                "Logged in successfully."
        );
    }

    @Override
    public BaseApiResponse<AuthTokenResponse> refreshToken(RefreshTokenRequest req) {
        RefreshToken storedRefreshToken = refreshTokenRepository.findByToken(req.refreshToken())
                .orElseThrow(RefreshTokenNotFoundException::new);
        if (storedRefreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(storedRefreshToken);
            throw new InvalidOrExpiredRefreshTokenException();
        }
        ApplicationUser user = storedRefreshToken.getUser();
        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);
        Long expiresIn = jwtService.getExpirationFromToken(newAccessToken);
        storedRefreshToken.setToken(newRefreshToken);
        refreshTokenRepository.save(storedRefreshToken);
        return BaseApiResponse.success(
                new AuthTokenResponse(newAccessToken, newRefreshToken, expiresIn),
                "Refreshed token successfully."
        );
    }

    @Override
    public BaseApiResponse<String> logout(String username) {
        ApplicationUser user = getUserByUsername(username);
        refreshTokenRepository.deleteByUser(user);
        return BaseApiResponse.success("Logged out successfully.");
    }

}
