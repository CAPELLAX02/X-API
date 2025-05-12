package com.x.backend.security;

import com.x.backend.security.password.PasswordEncodingConfig;
import com.x.backend.security.ratelimit.RateLimitingFilter;
import com.x.backend.security.servlet.CustomAccessDeniedHandler;
import com.x.backend.security.servlet.CustomAuthenticationEntryPoint;
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
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final RateLimitingFilter rateLimitingFilter;

    /*
     * Initializes the security configuration with required dependencies.
     */
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                          PasswordEncodingConfig passwordEncodingConfig,
                          UserServiceImpl userServiceImpl,
                          CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
                          CustomAccessDeniedHandler customAccessDeniedHandler,
                          RateLimitingFilter rateLimitingFilter
    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.passwordEncodingConfig = passwordEncodingConfig;
        this.userServiceImpl = userServiceImpl;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        this.rateLimitingFilter = rateLimitingFilter;
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

                        .requestMatchers(HttpMethod.POST,  "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST,  "/auth/email/verification/send").permitAll()
                        .requestMatchers(HttpMethod.POST,  "/auth/email/verification/resend").permitAll()
                        .requestMatchers(HttpMethod.PUT,   "/auth/verify").permitAll()
                        .requestMatchers(HttpMethod.PUT,   "/auth/phone").permitAll()
                        .requestMatchers(HttpMethod.PUT,   "/auth/update/phone").authenticated()
                        .requestMatchers(HttpMethod.PUT,   "/auth/password").permitAll()
                        .requestMatchers(HttpMethod.POST,  "/auth/email/password-recovery/send").permitAll()
                        .requestMatchers(HttpMethod.POST,  "/auth/email/password-recovery/resend").permitAll()
                        .requestMatchers(HttpMethod.PUT,   "/auth/recover-password").permitAll()
                        .requestMatchers(HttpMethod.PUT,   "/auth/update/password").authenticated()
                        .requestMatchers(HttpMethod.POST,  "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST,  "/auth/token/refresh").permitAll()
                        .requestMatchers(HttpMethod.POST,  "/auth/logout").authenticated()

                        .requestMatchers(HttpMethod.GET,   "/users/me").authenticated()
                        .requestMatchers(HttpMethod.PUT,   "/users/me/nickname").authenticated()
                        .requestMatchers(HttpMethod.PUT,   "/users/me/update/nickname").authenticated()
                        .requestMatchers(HttpMethod.GET,   "/users/nickname/{nickname}").permitAll()
                        .requestMatchers(HttpMethod.GET,   "/users/search").permitAll()
                        .requestMatchers(HttpMethod.PUT,   "/users/me/profile/bio").authenticated()
                        .requestMatchers(HttpMethod.PUT,   "/users/me/profile/location").authenticated()
                        .requestMatchers(HttpMethod.PUT,   "/users/me/profile/website").authenticated()

                        .requestMatchers(HttpMethod.GET,   "/users/me/privacy-settings").authenticated()
                        .requestMatchers(HttpMethod.PUT,   "/users/me/update/privacy-settings").authenticated()

                        .requestMatchers(HttpMethod.POST,  "/users/image/upload/pp").authenticated()
                        .requestMatchers(HttpMethod.POST,  "/users/image/upload/banner").authenticated()

                        .requestMatchers(HttpMethod.GET,   "/users/me/followers").authenticated()
                        .requestMatchers(HttpMethod.GET,   "/users/me/following").authenticated()
                        .requestMatchers(HttpMethod.PUT,   "/users/me/follow/{username}").authenticated()
                        .requestMatchers(HttpMethod.DELETE,"/users/me/unfollow/{username}").authenticated()
                        .requestMatchers(HttpMethod.GET,   "/users/me/check/{username}").authenticated()

                        .requestMatchers(HttpMethod.POST,  "/posts/create").authenticated()
                        .requestMatchers(HttpMethod.GET,   "/posts/{postId}").permitAll()
                        .requestMatchers(HttpMethod.GET,   "/author/{username}").permitAll()
                        .requestMatchers(HttpMethod.GET,   "/posts/create").authenticated()
                        .requestMatchers(HttpMethod.GET,   "/user/{username}/post-count").permitAll()

                        .requestMatchers(HttpMethod.PUT,   "/posts/{postId}/like").authenticated()
                        .requestMatchers(HttpMethod.DELETE,"/posts/{postId}/unlike").authenticated()
                        .requestMatchers(HttpMethod.GET,   "/posts/{postId}/likes").permitAll()

                        .requestMatchers(HttpMethod.POST,  "/posts/{postId}/comments").authenticated()
                        .requestMatchers(HttpMethod.PUT,   "/posts/{postId}/comments").authenticated()
                        .requestMatchers(HttpMethod.DELETE,"/posts/{postId}/comments").authenticated()
                        .requestMatchers(HttpMethod.GET,   "/posts/{postId}/comments").permitAll()

                        .requestMatchers(HttpMethod.PUT,   "/comments/{commentId}/like").authenticated()
                        .requestMatchers(HttpMethod.DELETE,"/comments/{commentId}/unlike").authenticated()
                        .requestMatchers(HttpMethod.GET,   "/comments/{commentId}/like-count").permitAll()

                        .requestMatchers(HttpMethod.POST,  "/posts/{postId}/repost").authenticated()
                        .requestMatchers(HttpMethod.DELETE,"/posts/{postId}/repost/undo").authenticated()
                        .requestMatchers(HttpMethod.GET,   "/posts/{postId}/count-repost").permitAll()
                        .requestMatchers(HttpMethod.GET,   "/posts/{postId}/repost/status").authenticated()

                        .requestMatchers(HttpMethod.POST,  "/posts/{postId}/bookmark").authenticated()
                        .requestMatchers(HttpMethod.DELETE,"/posts/{postId}/bookmark").authenticated()
                        .requestMatchers(HttpMethod.GET,   "/posts/{postId}/bookmark/status").authenticated()
                        .requestMatchers(HttpMethod.GET,   "/posts/{postId}/bookmark/count").permitAll()

                        .requestMatchers(HttpMethod.POST,  "/posts/{postId}/poll/vote").authenticated()
                        .requestMatchers(HttpMethod.DELETE,"/posts/{postId}/poll/revoke").authenticated()

                        .anyRequest().denyAll()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                )
                .addFilterBefore(
                        rateLimitingFilter,
                        UsernamePasswordAuthenticationFilter.class
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
