package com.x.backend.utils.common;

import com.x.backend.repositories.ApplicationUserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component
public class UsernameGenerationService {

    private final ApplicationUserRepository applicationUserRepository;

    public UsernameGenerationService(ApplicationUserRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }

    public String generateUniqueUsername(String firstName, String lastName) {
        String baseUsername = (firstName + lastName).toLowerCase().replaceAll("\\s+", "");
        String uniqueUsername = "";
        do {
            String randomSuffix = RandomStringUtils.randomAlphanumeric(10);
            uniqueUsername = baseUsername + randomSuffix;
        }
        while (applicationUserRepository.existsByUsername(uniqueUsername));
        return uniqueUsername;
    }

}
