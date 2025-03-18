package com.x.backend.config.security;

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

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final PasswordEncodingConfig passwordEncodingConfig;
    private final UserServiceImpl userServiceImpl;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, PasswordEncodingConfig passwordEncodingConfig, UserServiceImpl userServiceImpl) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.passwordEncodingConfig = passwordEncodingConfig;
        this.userServiceImpl = userServiceImpl;
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userServiceImpl);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncodingConfig.passwordEncoder());
        return new ProviderManager(daoAuthenticationProvider);
    }

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
                        .requestMatchers(HttpMethod.PUT,   "/auth/update/phone").permitAll()
                        .requestMatchers(HttpMethod.PUT,   "/auth/password").permitAll()
                        .requestMatchers(HttpMethod.POST,  "/auth/email/password-recovery/send").permitAll()
                        .requestMatchers(HttpMethod.POST,  "/auth/email/password-recovery/resend").permitAll()
                        .requestMatchers(HttpMethod.PUT,   "/auth/recover-password").permitAll()
                        .requestMatchers(HttpMethod.POST,  "/auth/token/refresh").permitAll()
                        .requestMatchers(HttpMethod.POST,  "/auth/login").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public SecureRandom secureRandom() {
        return new SecureRandom();
    }

}
