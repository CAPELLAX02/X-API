package com.x.backend.services.auth.strategies;

import com.x.backend.dto.auth.request.LoginRequest;
import com.x.backend.exceptions.auth.InvalidLoginCredentialsException;
import com.x.backend.exceptions.user.UserNotFoundByPhoneException;
import com.x.backend.models.entities.ApplicationUser;
import com.x.backend.repositories.ApplicationUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PhoneLoginStrategy implements LoginStrategy {

    private final ApplicationUserRepository applicationUserRepository;
    private final PasswordEncoder passwordEncoder;

    public PhoneLoginStrategy(ApplicationUserRepository applicationUserRepository, PasswordEncoder passwordEncoder) {
        this.applicationUserRepository = applicationUserRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public boolean supports(String loginType) {
        return "phone".equals(loginType);
    }

    @Override
    @Transactional
    public ApplicationUser authenticate(LoginRequest req) {
        ApplicationUser user = applicationUserRepository.findByEmail(req.phoneNumber())
                .orElseThrow(() -> new UserNotFoundByPhoneException(req.phoneNumber()));
        if (!passwordEncoder.matches(req.password(), user.getPassword())) {
            throw new InvalidLoginCredentialsException();
        }
        return user;
    }
}
