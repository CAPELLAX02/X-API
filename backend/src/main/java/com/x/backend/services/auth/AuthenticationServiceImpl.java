package com.x.backend.services.auth;

import com.x.backend.repositories.ApplicationUserRepository;
import com.x.backend.services.email.EmailService;
import com.x.backend.utils.common.UsernameGenerationService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    private final ApplicationUserRepository applicationUserRepository;
    private final UsernameGenerationService usernameGenerationService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationServiceImpl(
            ApplicationUserRepository applicationUserRepository,
            UsernameGenerationService usernameGenerationService,
            EmailService emailService,
            PasswordEncoder passwordEncoder
    ) {
        this.applicationUserRepository = applicationUserRepository;
        this.usernameGenerationService = usernameGenerationService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    // TODO: Implement methods
}
