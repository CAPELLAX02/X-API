package com.x.backend.services.auth;

import com.x.backend.dto.*;
import com.x.backend.exceptions.auth.InvalidLoginCredentialsException;
import com.x.backend.exceptions.auth.InvalidLoginRequestKeyException;
import com.x.backend.exceptions.auth.RefreshTokenIsEitherInvalidOrExpiredOrNotPresentException;
import com.x.backend.models.entities.ApplicationUser;
import com.x.backend.models.entities.RefreshToken;
import com.x.backend.repositories.ApplicationUserRepository;
import com.x.backend.repositories.RefreshTokenRepository;
import com.x.backend.services.auth.strategies.LoginStrategy;
import com.x.backend.services.email.MailService;
import com.x.backend.services.token.JwtService;
import com.x.backend.utils.api.BaseApiResponse;
import com.x.backend.utils.common.UsernameGenerationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    private final ApplicationUserRepository applicationUserRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UsernameGenerationService usernameGenerationService;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final List<LoginStrategy> loginStrategies;
    private final JwtService jwtService;

    public AuthenticationServiceImpl(
            ApplicationUserRepository applicationUserRepository,
            UsernameGenerationService usernameGenerationService,
            MailService mailService,
            PasswordEncoder passwordEncoder,
            List<LoginStrategy> loginStrategies,
            JwtService jwtService,
            RefreshTokenRepository refreshTokenRepository
    ) {
        this.applicationUserRepository = applicationUserRepository;
        this.usernameGenerationService = usernameGenerationService;
        this.mailService = mailService;
        this.passwordEncoder = passwordEncoder;
        this.loginStrategies = loginStrategies;
        this.jwtService = jwtService;
        this.refreshTokenRepository = refreshTokenRepository;
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
        if (!req.isValid()) {
            throw new InvalidLoginCredentialsException();
        }
        String loginKey = req.getLoginKey();
        LoginStrategy loginStrategy = loginStrategies.stream()
                .filter(s -> s.supports(loginKey))
                .findFirst()
                .orElseThrow(InvalidLoginRequestKeyException::new);
        ApplicationUser user = loginStrategy.authenticate(req);
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
        AuthTokenResponse authTokenResponse = new AuthTokenResponse(accessToken, refreshToken, expiresIn);
        BaseApiResponse<AuthTokenResponse> res = new BaseApiResponse<>(
                authTokenResponse,
                "Logged in successfully.",
                HttpStatus.OK
        );
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @Override
    public ResponseEntity<BaseApiResponse<AuthTokenResponse>> refreshToken(RefreshTokenRequest req) {
        RefreshToken storedRefreshToken = refreshTokenRepository.findByToken(req.refreshToken())
                .orElseThrow(RefreshTokenIsEitherInvalidOrExpiredOrNotPresentException::new);
        if (storedRefreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(storedRefreshToken);
            throw new RefreshTokenIsEitherInvalidOrExpiredOrNotPresentException();
        }
        ApplicationUser user = storedRefreshToken.getUser();
        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);
        Long expiresIn = jwtService.getExpirationFromToken(newAccessToken);
        storedRefreshToken.setToken(newRefreshToken);
        refreshTokenRepository.save(storedRefreshToken);
        AuthTokenResponse authTokenResponse = new AuthTokenResponse(
                newAccessToken,
                newRefreshToken,
                expiresIn
        );
        BaseApiResponse<AuthTokenResponse> res = new BaseApiResponse<>(
                authTokenResponse,
                "Token refreshed successfully.",
                HttpStatus.OK
        );
        return ResponseEntity.status(res.getStatus()).body(res);
    }

}
