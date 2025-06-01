package com.x.backend.services.user.block;

import com.x.backend.utils.api.BaseApiResponse;

import java.util.List;

public interface UserBlockService {


    BaseApiResponse<String> blockUser(String blockerUsername, String blockedUsername);


    BaseApiResponse<String> unblockUser(String blockerUsername, String blockedUsername);


    BaseApiResponse<Boolean> isUserBlocked(String actorUsername, String targetUsername);


    BaseApiResponse<List<String>> getBlockedUsers(String username);


    BaseApiResponse<List<String>> getUsersBlockingMe(String username);

}
