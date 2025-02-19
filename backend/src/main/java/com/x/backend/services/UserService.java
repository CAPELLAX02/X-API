package com.x.backend.services;

import com.x.backend.dto.request.RegistrationRequest;
import com.x.backend.exceptions.EmailAlreadyTakenException;
import com.x.backend.exceptions.EmailFailedToSentException;
import com.x.backend.exceptions.InvalidOrExpiredVerificationCode;
import com.x.backend.exceptions.UserDoesNotExistException;
import com.x.backend.models.ApplicationUser;
import com.x.backend.models.Role;
import com.x.backend.repositories.RoleRepository;
import com.x.backend.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, MailService mailService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.mailService = mailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ApplicationUser user = getUserByUsername(username);
        Set<GrantedAuthority> authorities = user.getAuthorities()
                .stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                .collect(Collectors.toSet());
        UserDetails userDetails = new User(user.getUsername(), user.getPassword(), authorities);
        return userDetails;
    }

    public ApplicationUser getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(UserDoesNotExistException::new);
    }

    public ApplicationUser registerUser(RegistrationRequest registrationRequest) {
        if (userRepository.existsByEmail(registrationRequest.email())) {
            throw new EmailAlreadyTakenException();
        }

        ApplicationUser user = new ApplicationUser();
        user.setFirstName(registrationRequest.firstName());
        user.setLastName(registrationRequest.lastName());
        user.setEmail(registrationRequest.email());
        user.setDateOfBirth(registrationRequest.dateOfBirth());

        user.setUsername(user.getFirstName() + user.getLastName() + (long) Math.floor(Math.random() * 1_000_000));

        Set<Role> roles = user.getAuthorities();
        Role role = roleRepository.findByAuthority("USER")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setAuthority("USER");
                    return roleRepository.save(newRole);
                });

        roles.add(role);
        user.setAuthorities(roles);

        return userRepository.save(user);
    }

    public void generateEmailVerification(String username) throws EmailFailedToSentException {
        ApplicationUser user = getUserByUsername(username);
        user.setVerificationCode(generateVerificationCode());
        mailService.sendVerificationCodeViaEmail(user.getEmail(), user.getVerificationCode().toString());
        userRepository.save(user);
    }

    private Long generateVerificationCode() {
        return (long) Math.floor(Math.random() * 100_000_000);
    }

    public ApplicationUser verifyEmail(String username, Long verificationCode) {
        ApplicationUser user = getUserByUsername(username);
        if (!user.getVerificationCode().equals(verificationCode)) {
            throw new InvalidOrExpiredVerificationCode();
        }
        user.setEnabled(true);
        user.setVerificationCode(null);
        return userRepository.save(user);
    }

    public ApplicationUser setPassword(String username, String password) {
        ApplicationUser user = getUserByUsername(username);
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    public ApplicationUser updateUser(ApplicationUser user) {
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            throw new EmailAlreadyTakenException();
        }
    }

}
