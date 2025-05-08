package com.x.backend.security.password;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration class for password encryption using the Argon2 algorithm.
 * <p>
 * Argon2 is a memory-hard hashing function recommended for modern password security.
 */
@Configuration
public class PasswordEncodingConfig {

    /**
     * Creates a password encoder bean using Argon2 with strong parameters.
     * @return a configured instance of Argon2PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Parameters: salt length, hash length, parallelism, memory, iterations
        return new Argon2PasswordEncoder(16, 32, 1, 65536, 3);
    }

}
