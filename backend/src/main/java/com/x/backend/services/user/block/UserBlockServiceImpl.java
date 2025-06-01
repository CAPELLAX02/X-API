package com.x.backend.services.user.block;

import com.x.backend.exceptions.user.UserAlreadyBlockedException;
import com.x.backend.exceptions.user.UserHaveNotBlockedException;
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
        ApplicationUser blocker = getUserByUsername(blockerUsername);
        ApplicationUser blocked = getUserByUsername(blockedUsername);

        if (userBlockRepository.existsByBlockingUserAndBlockedUser(blocker, blocked)) {
            throw new UserAlreadyBlockedException();
        }

        UserBlock block = new UserBlock();
        block.setBlockingUser(blocker);
        block.setBlockedUser(blocked);
        block.setBlockedAt(LocalDateTime.now());

        userBlockRepository.save(block);

        return BaseApiResponse.success("User has been blocked.");
    }

    @Override
    public BaseApiResponse<String> unblockUser(String blockerUsername, String blockedUsername) {
        ApplicationUser blocker = getUserByUsername(blockerUsername);
        ApplicationUser blocked = getUserByUsername(blockedUsername);

        if (!userBlockRepository.existsByBlockingUserAndBlockedUser(blocker, blocked)) {
            throw new UserHaveNotBlockedException();
        }

        userBlockRepository.findByBlockingUserAndBlockedUser(blocker, blocked)
                .ifPresent(userBlockRepository::delete);

        return BaseApiResponse.success("User has been unblocked.");
    }

    @Override
    public BaseApiResponse<Boolean> isUserBlocked(String actorUsername, String targetUsername) {
        ApplicationUser actor = getUserByUsername(actorUsername);
        ApplicationUser target = getUserByUsername(targetUsername);

        boolean isBlocked = userBlockRepository.existsByBlockingUserAndBlockedUser(actor, target)
                || userBlockRepository.existsByBlockingUserAndBlockedUser(target, actor);

        return BaseApiResponse.success(isBlocked);
    }

    @Override
    public BaseApiResponse<List<String>> getBlockedUsers(String username) {
        ApplicationUser user = getUserByUsername(username);

        List<String> blockedUsernames = userBlockRepository
                .findAllByBlockingUser(user)
                .stream()
                .map(block -> block.getBlockedUser().getUsername())
                .toList();

        return BaseApiResponse.success(blockedUsernames);
    }

    @Override
    public BaseApiResponse<List<String>> getUsersBlockingMe(String username) {
        ApplicationUser user = getUserByUsername(username);

        List<String> usersBlockingMe = userBlockRepository
                .findAllByBlockedUser(user)
                .stream()
                .map(block -> block.getBlockingUser().getUsername())
                .toList();

        return BaseApiResponse.success(usersBlockingMe);
    }

}
