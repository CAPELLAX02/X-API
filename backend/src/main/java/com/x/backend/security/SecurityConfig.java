package com.x.backend.security;

import com.x.backend.services.token.filter.JwtAuthenticationFilter;
import com.x.backend.services.user.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.SecureRandom;

/**
 * SecurityConfig manages authentication, authorization, and data security.
 * <p>
 * This includes AES-256 CBC encryption for request/response (if required),
 * JWT authentication for session management, and password encoding for storage.
 * </p>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final PasswordEncodingConfig passwordEncodingConfig;
    private final UserServiceImpl userServiceImpl;

    /**
     * Initializes the security configuration with required dependencies.
     *
     * @param jwtAuthenticationFilter Handles JWT-based authentication.
     * @param passwordEncodingConfig  Configures password encoding mechanisms.
     * @param userServiceImpl         Provides user authentication services.
     */
    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            PasswordEncodingConfig passwordEncodingConfig,
            UserServiceImpl userServiceImpl
    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.passwordEncodingConfig = passwordEncodingConfig;
        this.userServiceImpl = userServiceImpl;
    }

    /**
     * Creates and registers an AuthenticationManager bean for authentication handling.
     *
     * @return AuthenticationManager instance.
     */
    @Bean
    public AuthenticationManager authenticationManagerBean() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userServiceImpl);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncodingConfig.passwordEncoder());
        return new ProviderManager(daoAuthenticationProvider);
    }

    /**
     * Provides a SecureRandom instance for cryptographic operations.
     *
     * @return SecureRandom instance.
     */
    @Bean
    public SecureRandom secureRandom() {
        return new SecureRandom();
    }

    /**
     * Defines security rules, including JWT authentication and AES-256 request encryption.
     * Note the usage of DecryptionFilter -> JWT Filter -> EncryptionFilter.
     * DecryptionFilter must come before JWT so the request is already decrypted
     * when JWT tries to parse JSON or tokens.
     *
     * @param httpSecurity HttpSecurity configuration object.
     * @return Configured SecurityFilterChain.
     * @throws Exception if security configuration fails.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/email/verification/send").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/email/verification/resend").permitAll()
                        .requestMatchers(HttpMethod.PUT,  "/auth/verify").permitAll()
                        .requestMatchers(HttpMethod.PUT,  "/auth/phone").permitAll()
                        .requestMatchers(HttpMethod.PUT,  "/auth/update/phone").hasAuthority("ROLE_USER")
                        .requestMatchers(HttpMethod.PUT,  "/auth/password").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/email/password-recovery/send").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/email/password-recovery/resend").permitAll()
                        .requestMatchers(HttpMethod.PUT,  "/auth/recover-password").permitAll()
                        .requestMatchers(HttpMethod.PUT,  "/auth/update/password").hasAuthority("ROLE_USER")
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/token/refresh").permitAll()

                        .requestMatchers(HttpMethod.GET,  "/users/me").hasAuthority("ROLE_USER")

                        .anyRequest().authenticated()
                )
                // 1) Decrypt incoming request body
//                .addFilterBefore(
//                        decryptionFilter(),
//                        UsernamePasswordAuthenticationFilter.class
//                )
                // 2) JWT authentication
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
                // 3) Encrypt outgoing response body
//                .addFilterAfter(
//                        encryptionFilter(),
//                        UsernamePasswordAuthenticationFilter.class
//                )
                .build();
    }

//    /**
//     * Instantiate the DecryptionFilter with the actual SecretKey and IV (IvParameterSpec).
//     *
//     * @return DecryptionFilter instance.
//     */
//    @Bean
//    public DecryptionFilter decryptionFilter() {
//        // Instead of .toString(), we pass the actual IvParameterSpec
//        SecretKey key = AESUtil.getSecretKey();
//        IvParameterSpec iv = AESUtil.getIv();
//        return new DecryptionFilter(key, iv);    }
//
//    /**
//     * Instantiate the EncryptionFilter with the actual SecretKey and IV (IvParameterSpec).
//     *
//     * @return EncryptionFilter instance.
//     */
//    @Bean
//    public EncryptionFilter encryptionFilter() {
//        SecretKey key = AESUtil.getSecretKey();
//        IvParameterSpec iv = AESUtil.getIv();
//        return new EncryptionFilter(key, iv);
//    }

}
