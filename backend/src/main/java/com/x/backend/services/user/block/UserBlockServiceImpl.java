package com.x.backend.services.user.block;

import com.x.backend.exceptions.user.UserAlreadyBlockedException;
import com.x.backend.models.user.ApplicationUser;
import com.x.backend.models.user.UserBlock;
import com.x.backend.repositories.ApplicationUserRepository;
import com.x.backend.repositories.UserBlockRepository;
import com.x.backend.utils.api.BaseApiResponse;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserBlockServiceImpl implements UserBlockService {

    private final ApplicationUserRepository applicationUserRepository;
    private final UserBlockRepository userBlockRepository;

    public UserBlockServiceImpl(ApplicationUserRepository applicationUserRepository,
                                UserBlockRepository userBlockRepository
    ) {
        this.applicationUserRepository = applicationUserRepository;
        this.userBlockRepository = userBlockRepository;
    }

    private ApplicationUser getUserByUsername(String username) {
        return applicationUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Override
    public BaseApiResponse<String> blockUser(String blockerUsername, String blockedUsername) {
        return null;
    }

    @Override
    public BaseApiResponse<String> unblockUser(String blockerUsername, String blockedUsername) {
        return null;
    }

    @Override
    public BaseApiResponse<Boolean> isUserBlocked(String actorUsername, String targetUsername) {
        return null;
    }

    @Override
    public BaseApiResponse<List<String>> getBlockedUsernames(String username) {
        return null;
    }

    @Override
    public BaseApiResponse<List<String>> getUsersBlockingMe(String username) {
        return null;
    }

}
