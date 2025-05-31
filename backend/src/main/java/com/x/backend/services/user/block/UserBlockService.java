package com.x.backend.services.user.block;

import java.util.List;

public interface UserBlockService {


    void blockUser(String blockerUsername, String blockedUsername);


    void unblockUser(String blockerUsername, String blockedUsername);


    boolean isUserBlocked(String actorUsername, String targetUsername);


    List<String> getBlockedUsernames(String username);


    List<String> getUsersBlockingMe(String username);

}
