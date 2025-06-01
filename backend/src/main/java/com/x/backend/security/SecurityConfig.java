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

                        // Register User
                        .requestMatchers(HttpMethod.POST,  "/auth/register").permitAll()
                        // Send Verification Email
                        .requestMatchers(HttpMethod.POST,  "/auth/email/verification/send").permitAll()
                        // Resend Verification Email
                        .requestMatchers(HttpMethod.POST,  "/auth/email/verification/resend").permitAll()
                        // Complete Email Verification
                        .requestMatchers(HttpMethod.PUT,   "/auth/verify").permitAll()
                        // Set Phone Number
                        .requestMatchers(HttpMethod.PUT,   "/auth/phone").permitAll()
                        // Change Phone Number
                        .requestMatchers(HttpMethod.PUT,   "/auth/update/phone").authenticated()
                        // Set Password
                        .requestMatchers(HttpMethod.PUT,   "/auth/password").permitAll()
                        // Send Password Recovery Email
                        .requestMatchers(HttpMethod.POST,  "/auth/email/password-recovery/send").permitAll()
                        // Resend Password Recovery Email
                        .requestMatchers(HttpMethod.POST,  "/auth/email/password-recovery/resend").permitAll()
                        // Recover Password
                        .requestMatchers(HttpMethod.PUT,   "/auth/recover-password").permitAll()
                        // Change Password
                        .requestMatchers(HttpMethod.PUT,   "/auth/update/password").authenticated()
                        // Login
                        .requestMatchers(HttpMethod.POST,  "/auth/login").permitAll()
                        // Refresh Token
                        .requestMatchers(HttpMethod.POST,  "/auth/token/refresh").permitAll()
                        // Logout
                        .requestMatchers(HttpMethod.POST,  "/auth/logout").authenticated()

                        // Get Current User
                        .requestMatchers(HttpMethod.GET,   "/users/me").authenticated()
                        // Set Nickname
                        .requestMatchers(HttpMethod.PUT,   "/users/me/nickname").authenticated()
                        // Change Nickname
                        .requestMatchers(HttpMethod.PUT,   "/users/me/update/nickname").authenticated()
                        // Get Users by Nickname
                        .requestMatchers(HttpMethod.GET,   "/users/nickname/{nickname}").permitAll()
                        // Search Users by Nickname
                        .requestMatchers(HttpMethod.GET,   "/users/search").permitAll()
                        // Update Biography
                        .requestMatchers(HttpMethod.PUT,   "/users/me/profile/bio").authenticated()
                        // Update Location
                        .requestMatchers(HttpMethod.PUT,   "/users/me/profile/location").authenticated()
                        // Update Website
                        .requestMatchers(HttpMethod.PUT,   "/users/me/profile/website").authenticated()

                        // Get Privacy Settings
                        .requestMatchers(HttpMethod.GET,   "/users/me/privacy-settings").authenticated()
                        // Update Privacy Settings
                        .requestMatchers(HttpMethod.PUT,   "/users/me/update/privacy-settings").authenticated()

                        // Upload Profile Image
                        .requestMatchers(HttpMethod.POST,  "/users/image/upload/pp").authenticated()
                        // Upload Banner Image
                        .requestMatchers(HttpMethod.POST,  "/users/image/upload/banner").authenticated()

                        // Get All Followers
                        .requestMatchers(HttpMethod.GET,   "/users/me/followers").authenticated()
                        // Get All Followings
                        .requestMatchers(HttpMethod.GET,   "/users/me/following").authenticated()
                        // Follow User
                        .requestMatchers(HttpMethod.PUT,   "/users/me/follow/{username}").authenticated()
                        // Unfollow User
                        .requestMatchers(HttpMethod.DELETE,"/users/me/unfollow/{username}").authenticated()
                        // Is Following
                        .requestMatchers(HttpMethod.GET,   "/users/me/check/{username}").authenticated()

                        // Create Post
                        .requestMatchers(HttpMethod.POST,  "/posts/create").authenticated()
                        // Get Post by ID
                        .requestMatchers(HttpMethod.GET,   "/posts/{postId}").permitAll()
                        // Get Posts by Author
                        .requestMatchers(HttpMethod.GET,   "/posts/author/{username}").permitAll()
                        // Get Timeline
                        .requestMatchers(HttpMethod.GET,   "/posts/timeline").authenticated()
                        // Get User Post Count
                        .requestMatchers(HttpMethod.GET,   "/user/{username}/post-count").permitAll()

                        // Like Post
                        .requestMatchers(HttpMethod.PUT,   "/posts/{postId}/like").authenticated()
                        // Unlike Post
                        .requestMatchers(HttpMethod.DELETE,"/posts/{postId}/unlike").authenticated()
                        // Get Post Like Count
                        .requestMatchers(HttpMethod.GET,   "/posts/{postId}/likes").permitAll()

                        // Create Comment
                        .requestMatchers(HttpMethod.POST,  "/posts/{postId}/comments").authenticated()
                        // Get Post Comments
                        .requestMatchers(HttpMethod.GET,   "/posts/{postId}/comments").permitAll()
                        // Edit Comment
                        .requestMatchers(HttpMethod.PUT,   "/comments/{commentId}").authenticated()
                        // Delete Comment
                        .requestMatchers(HttpMethod.DELETE,"/comments/{commentId}").authenticated()
                        // Create Sub-Comment
                        .requestMatchers(HttpMethod.POST,  "/posts/{postId}/comments/{commentId}/replies").authenticated()
                        // Edit Sub-Comment
                        .requestMatchers(HttpMethod.PUT,   "/sub-comments/{subCommentId}").authenticated()
                        // Delete Sub-Comment
                        .requestMatchers(HttpMethod.DELETE,"/sub-comments/{subCommentId}").authenticated()

                        // Like Comment
                        .requestMatchers(HttpMethod.PUT,   "/comments/{commentId}/like").authenticated()
                        // Unlike Comment
                        .requestMatchers(HttpMethod.DELETE,"/comments/{commentId}/unlike").authenticated()
                        // Get Comment Like
                        .requestMatchers(HttpMethod.GET,   "/comments/{commentId}/like-count").permitAll()

                        // Repost Post
                        .requestMatchers(HttpMethod.POST,  "/posts/{postId}/repost").authenticated()
                        // Undo Post Repost
                        .requestMatchers(HttpMethod.DELETE,"/posts/{postId}/repost/undo").authenticated()
                        // Get Post Repost Count
                        .requestMatchers(HttpMethod.GET,   "/posts/{postId}/count-repost").permitAll()
                        // Has Post Reposted
                        .requestMatchers(HttpMethod.GET,   "/posts/{postId}/repost/status").authenticated()

                        // Bookmark Post
                        .requestMatchers(HttpMethod.POST,  "/posts/{postId}/bookmark").authenticated()
                        // Remove Post Bookmark
                        .requestMatchers(HttpMethod.DELETE,"/posts/{postId}/bookmark").authenticated()
                        // Has User Bookmarked
                        .requestMatchers(HttpMethod.GET,   "/posts/{postId}/bookmark/status").authenticated()
                        // Get Post Bookmark Count
                        .requestMatchers(HttpMethod.GET,   "/posts/{postId}/bookmark/count").permitAll()

                        // Vote in to Poll
                        .requestMatchers(HttpMethod.POST,  "/posts/{postId}/poll/vote").authenticated()
                        // Revoke Vote Poll
                        .requestMatchers(HttpMethod.DELETE,"/posts/{postId}/poll/revoke").authenticated()

                        // Report User
                        .requestMatchers(HttpMethod.POST,  "/report/user/{id}").authenticated()
                        // Report Post
                        .requestMatchers(HttpMethod.POST,  "/report/post/{id}").authenticated()
                        // Report Comment
                        .requestMatchers(HttpMethod.POST,  "/report/comment/{id}").authenticated()

                        // Block User
                        .requestMatchers(HttpMethod.POST,  "/users/{username}/block").authenticated()
                        // Unblock User
                        .requestMatchers(HttpMethod.DELETE,"/users/{username}/unblock").authenticated()
                        // Is User Blocked
                        .requestMatchers(HttpMethod.GET,   "/users/{username}/is-blocked").authenticated()
                        // Get Blocked Users
                        .requestMatchers(HttpMethod.GET,   "/users/blocked").authenticated()
                        // Get Users Who Blocked Me
                        .requestMatchers(HttpMethod.GET,   "/users/blocked-me").authenticated()

                        // WebSocket Connection Permission
                        .requestMatchers("/ws/**").permitAll()

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
