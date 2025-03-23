package com.x.backend.services.user.follow;

import com.x.backend.dto.user.response.UserResponse;
import com.x.backend.utils.api.BaseApiResponse;

import java.util.List;

public interface FollowService {

    BaseApiResponse<List<UserResponse>> getAllFollowers(String currentUsername);
    BaseApiResponse<List<UserResponse>> getAllFollowings(String currentUsername);

    BaseApiResponse<String> followUser(String currentUsername, String targetUsername);
    BaseApiResponse<String> unfollowUser(String username, String targetUsername);
    BaseApiResponse<Boolean> isFollowing(String currentUsername, String targetUsername);

}
