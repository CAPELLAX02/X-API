package com.x.backend.services.user;

import com.x.backend.dto.authentication.request.RegisterUserRequest;
import com.x.backend.exceptions.EmailAlreadyTakenException;
import com.x.backend.exceptions.EmailFailedToSentException;
import com.x.backend.exceptions.InvalidOrExpiredVerificationCode;
import com.x.backend.exceptions.UserDoesNotExistException;
import com.x.backend.models.ApplicationUser;
import com.x.backend.models.Role;
import com.x.backend.repositories.RoleRepository;
import com.x.backend.repositories.UserRepository;
import com.x.backend.services.mail.MailService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, MailService mailService, PasswordEncoder passwordEncoder) {
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
        return new User(user.getUsername(), user.getPassword(), authorities);
    }

    @Override
    public ApplicationUser getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(UserDoesNotExistException::new);
    }

    private String generateUsername(String firstName, String lastName) {
        return firstName + lastName + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Override
    public ApplicationUser registerUser(RegisterUserRequest registerUserRequest) {
        if (userRepository.existsByEmail(registerUserRequest.email())) {
            throw new EmailAlreadyTakenException();
        }

        ApplicationUser user = new ApplicationUser();
        user.setFirstName(registerUserRequest.firstName());
        user.setLastName(registerUserRequest.lastName());
        user.setEmail(registerUserRequest.email());
        user.setDateOfBirth(registerUserRequest.dateOfBirth());

        String uniqueUsername = generateUsername(registerUserRequest.firstName(), registerUserRequest.lastName());
        user.setUsername(uniqueUsername);

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

    @Override
    public void generateEmailVerification(String username) throws EmailFailedToSentException {
        ApplicationUser user = getUserByUsername(username);
        user.setVerificationCode(generateVerificationCode());
        mailService.sendVerificationCodeViaEmail(user.getEmail(), user.getVerificationCode());
        userRepository.save(user);
    }

    private String generateVerificationCode() {
        return UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    @Override
    public ApplicationUser verifyEmail(String username, String verificationCode) {
        ApplicationUser user = getUserByUsername(username);
        if (!user.getVerificationCode().equals(verificationCode)) {
            throw new InvalidOrExpiredVerificationCode();
        }
        user.setEnabled(true);
        user.setVerificationCode(null);
        return userRepository.save(user);
    }

    @Override
    public ApplicationUser setPassword(String username, String password) {
        ApplicationUser user = getUserByUsername(username);
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    @Override
    public ApplicationUser updateUser(ApplicationUser user) {
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            throw new EmailAlreadyTakenException();
        }
    }

}
