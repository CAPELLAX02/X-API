package com.x.backend.services;

import com.x.backend.dto.request.RegistrationRequest;
import com.x.backend.exceptions.EmailAlreadyTakenException;
import com.x.backend.exceptions.UserDoesNotExistException;
import com.x.backend.models.ApplicationUser;
import com.x.backend.models.Role;
import com.x.backend.repositories.RoleRepository;
import com.x.backend.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public ApplicationUser updateUser(ApplicationUser user) {
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            throw new EmailAlreadyTakenException();
        }
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

}
