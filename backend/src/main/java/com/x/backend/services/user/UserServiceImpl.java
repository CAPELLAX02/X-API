package com.x.backend.services.user;

import com.x.backend.dto.*;
import com.x.backend.dto.LoginResponse;
import com.x.backend.exceptions.*;
import com.x.backend.models.ApplicationUser;
import com.x.backend.models.Image;
import com.x.backend.models.Role;
import com.x.backend.repositories.RoleRepository;
import com.x.backend.repositories.UserRepository;
import com.x.backend.services.image.ImageService;
import com.x.backend.services.mail.MailService;
import com.x.backend.services.token.JwtService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final MailService mailService;
    private final ImageService imageService;
    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public UserServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            MailService mailService,
            PasswordEncoder passwordEncoder,
            ImageService imageService,
            JwtService jwtService,
            @Lazy AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.mailService = mailService;
        this.passwordEncoder = passwordEncoder;
        this.imageService = imageService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
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
    public ApplicationUser getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserDoesNotExistException::new);
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

        Set<Role> roles = user.getAuthoritiesSet();
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
    public String startEmailVerification(StartEmailVerificationRequest startEmailVerificationRequest) throws EmailFailedToSentException {
        ApplicationUser user = getUserByUsername(startEmailVerificationRequest.username());
        user.setVerificationCode(generateVerificationCode());
        mailService.sendVerificationCodeViaEmail(user.getEmail(), user.getVerificationCode());
        userRepository.save(user);
        return "Verification code has been generated and sent to " + user.getEmail();
    }

    private String generateVerificationCode() {
        return UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    @Override
    public ApplicationUser completeEmailVerification(CompleteEmailVerificationRequest completeEmailVerificationRequest) {
        ApplicationUser user = getUserByUsername(completeEmailVerificationRequest.username());
        if (!user.getVerificationCode().equals(completeEmailVerificationRequest.verificationCode())) {
            throw new InvalidOrExpiredVerificationCode();
        }
        user.setEnabled(true);
        user.setVerificationCode(null);
        return userRepository.save(user);
    }

    @Override
    public ApplicationUser updateUserPassword(UpdatePasswordRequest updatePasswordRequest) {
        ApplicationUser user = getUserByUsername(updatePasswordRequest.username());
        String encodedPassword = passwordEncoder.encode(updatePasswordRequest.password());
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

    @Override
    public ApplicationUser updateUserPhoneNumber(UpdatePhoneNumberRequest updatePhoneNumberRequest) {
        String phoneNumber = updatePhoneNumberRequest.phoneNumber();
        ApplicationUser user = getUserByUsername(updatePhoneNumberRequest.username());
        user.setPhoneNumber(phoneNumber);
        return userRepository.save(user);
    }

    @Override
    public ApplicationUser setProfileOrBanner(String username, MultipartFile file, String prefix) {
        ApplicationUser user = getUserByUsername(username);
        Image image = imageService.uploadImage(file, prefix);
        try {
            if (prefix.equals("profile_picture")) {
                if (user.getProfilePicture() != null || !user.getProfilePicture().getImageName().equals("default_profile_picture.png")) {
                    Path path = Paths.get(user.getProfilePicture().getImagePath());
                    Files.deleteIfExists(path);
                }
                user.setProfilePicture(image);
            }
            else if (prefix.equals("banner_picture")) {
                if (user.getBannerPicture() != null || !user.getBannerPicture().getImageName().equals("default_banner_picture.png")) {
                    Path path = Paths.get(user.getBannerPicture().getImagePath());
                    Files.deleteIfExists(path);
                }
                user.setBannerPicture(image);
            }
            else {
                throw new InvalidImagePrefixException();
            }
        }
        catch (IOException e) {
            throw new UnableToUploadFileException();
        }
        return userRepository.save(user);
    }

    @Override
    public LoginResponse loginUser(LoginRequest loginRequest) {
        String username = loginRequest.username();
        String password = loginRequest.password();

        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            String accessToken = jwtService.generateToken(authentication);
            return new LoginResponse(accessToken);

        } catch (Exception e) {
            return new LoginResponse(null);
        }
    }

    @Override
    public Set<ApplicationUser> followUser(String followerUsername, String followeeUsername) {
        ApplicationUser loggedInUser = getUserByUsername(followerUsername);
        ApplicationUser followedUser = getUserByUsername(followeeUsername);

        if (loggedInUser.equals(followedUser)) {
            throw new CannotFollowThemselvesException();
        }

        if (!loggedInUser.getFollowing().contains(followedUser)) {
            loggedInUser.getFollowing().add(followedUser);
            followedUser.getFollowers().add(loggedInUser);

            userRepository.save(loggedInUser);
            userRepository.save(followedUser);
        }

        return loggedInUser.getFollowing();
    }

    @Override
    public Set<ApplicationUser> getFollowings(String username) {
        ApplicationUser user = getUserByUsername(username);
        return user.getFollowing();
    }

    @Override
    public Set<ApplicationUser> getFollowers(String username) {
        ApplicationUser user = getUserByUsername(username);
        return user.getFollowers();
    }




}
