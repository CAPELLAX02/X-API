package com.x.backend.services.user.block;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserBlockServiceImpl implements UserBlockService {

    @Override
    public void blockUser(String blockerUsername, String blockedUsername) {

    }

    @Override
    public void unblockUser(String blockerUsername, String blockedUsername) {

    }

    @Override
    public boolean isUserBlocked(String actorUsername, String targetUsername) {
        return false;
    }

    @Override
    public List<String> getBlockedUsernames(String username) {
        return List.of();
    }

    @Override
    public List<String> getUsersBlockingMe(String username) {
        return List.of();
    }

}
