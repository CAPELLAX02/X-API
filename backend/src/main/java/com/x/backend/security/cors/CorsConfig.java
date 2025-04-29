package com.x.backend.security.cors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Configuration class for Cross-Origin Resource Sharing (CORS) settings.
 *
 * <p>This configuration defines the allowed origins, methods, and headers
 * for cross-origin requests. Ensure that wildcard (*) values are replaced
 * with specific origins in the production environment.</p>
 *
 * <p>For development, a permissive configuration is used to prevent conflicts.
 * In production, update the allowed origins to match the deployed frontend.</p>
 */
@Configuration
@EnableWebSecurity
public class CorsConfig {

    /**
     * Defines CORS policy for the application.
     *
     * <p>Modifications may be required based on security policies.
     * Ensure proper restrictions on allowed origins, headers, and methods
     * in a production environment.</p>
     *
     * @return a configured {@link CorsConfigurationSource} instance
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Allowed origins (update for production use)
        configuration.setAllowedOriginPatterns(List.of("http://localhost:3000", "*"));

        // HTTP methods allowed for cross-origin requests
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Allowed headers (consider specifying explicitly in production)
        configuration.setAllowedHeaders(List.of("*"));

        // Allow credentials (required for authentication mechanisms)
        configuration.setAllowCredentials(true);

        // Cache duration for CORS preflight requests
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
