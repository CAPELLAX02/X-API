package com.x.backend.services;

import com.x.backend.dto.request.RegistrationRequest;
import com.x.backend.exceptions.EmailAlreadyTakenException;
import com.x.backend.models.ApplicationUser;
import com.x.backend.models.Role;
import com.x.backend.repositories.RoleRepository;
import com.x.backend.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public ApplicationUser registerUser(RegistrationRequest registrationRequest) {
        ApplicationUser user = new ApplicationUser();
        user.setFirstName(registrationRequest.firstName());
        user.setLastName(registrationRequest.lastName());
        user.setEmail(registrationRequest.email());
        user.setDateOfBirth(registrationRequest.dateOfBirth());

        String name = user.getFirstName() + user.getLastName();
        boolean nameTaken = true;
        String tempName;
        while (nameTaken) {
            tempName = generateUsername(name);
            if (userRepository.findByUsername(tempName).isEmpty()) {
                nameTaken = false;
            }
        }

        Set<Role> roles = user.getAuthorities();
        Role role = roleRepository.findByAuthority("USER")
                        .orElseGet(() -> {
                            Role newRole = new Role();
                            newRole.setAuthority("USER");
                            return roleRepository.save(newRole);
                        });

        roles.add(role);
        user.setAuthorities(roles);

        try {
            return userRepository.save(user);
        } catch (Exception e) {
            throw new EmailAlreadyTakenException();
        }
    }

    private String generateUsername(String name) {
        long generatedNumber = (long) Math.floor(Math.random() * 1_000_000);
        return name + generatedNumber;
    }

}
