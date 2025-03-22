package com.x.backend.services.user;

import com.x.backend.dto.auth.response.UserResponse;
import com.x.backend.models.entities.ApplicationUser;
import com.x.backend.repositories.ApplicationUserRepository;
import com.x.backend.utils.api.BaseApiResponse;
import com.x.backend.utils.builder.UserResponseBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    private final ApplicationUserRepository applicationUserRepository;
    private final UserResponseBuilder userResponseBuilder;

    public UserServiceImpl(
            ApplicationUserRepository applicationUserRepository,
            UserResponseBuilder userResponseBuilder
    ) {
        this.applicationUserRepository = applicationUserRepository;
        this.userResponseBuilder = userResponseBuilder;
    }

    @Override
    public ApplicationUser getUserByUsername(String username) {
        return applicationUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserByUsername(username);
    }

    @Override
    public BaseApiResponse<UserResponse> getCurrentUser(String username) {
        ApplicationUser user = getUserByUsername(username);
        UserResponse userRes = userResponseBuilder.buildUserResponse(user);
        return BaseApiResponse.success(
                userRes,
                "Current user retrieved successfully."
        );
    }

}
