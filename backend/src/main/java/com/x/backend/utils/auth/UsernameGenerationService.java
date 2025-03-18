package com.x.backend.utils.auth;

import com.x.backend.repositories.ApplicationUserRepository;
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;

@Component
public class UsernameGenerationService {

    private final ApplicationUserRepository applicationUserRepository;
    private final SecureRandom secureRandom;

    public UsernameGenerationService(ApplicationUserRepository applicationUserRepository, SecureRandom secureRandom) {
        this.applicationUserRepository = applicationUserRepository;
        this.secureRandom = secureRandom;
    }

    public String generateUniqueUsername(String firstName, String lastName) {
        String baseUsername = (firstName + lastName).toLowerCase().replaceAll("\\s+", "");
        String uniqueUsername;

        do {
            String salt = generateSalt();
            String hash = Sha512DigestUtils.shaHex(salt + baseUsername);
            uniqueUsername = baseUsername + hash.substring(0, 8);
        }
        while (applicationUserRepository.existsByUsername(uniqueUsername));

        return uniqueUsername;
    }

    private String generateSalt() {
        byte[] salt = new byte[8];
        secureRandom.nextBytes(salt);
        return Base64.getEncoder().withoutPadding().encodeToString(salt);
    }

}
