package com.x.backend.services.user.follow;

import com.x.backend.dto.user.response.UserResponse;
import com.x.backend.utils.api.BaseApiResponse;

import java.util.List;

/**
 * Service interface responsible for managing follow relationships between users.
 * Provides functionality to follow/unfollow users, check follow status, and retrieve follower/following lists.
 */
public interface FollowService {

    /**
     * Retrieves all users who follow the specified user.
     *
     * @param currentUsername the username of the user whose followers are being fetched
     * @return a list of users following the current user, wrapped in a successful {@link BaseApiResponse}
     */
    BaseApiResponse<List<UserResponse>> getAllFollowers(String currentUsername);

    /**
     * Retrieves all users followed by the specified user.
     *
     * @param currentUsername the username of the user whose followings are being fetched
     * @return a list of users the current user is following, wrapped in a successful {@link BaseApiResponse}
     */
    BaseApiResponse<List<UserResponse>> getAllFollowings(String currentUsername);

    /**
     * Follows a target user on behalf of the current user.
     *
     * @param currentUsername the username of the acting user
     * @param targetUsername the username of the user to be followed
     * @return a success message if the follow action is performed,
     *         or an error message if already following or trying to follow oneself
     */
    BaseApiResponse<String> followUser(String currentUsername, String targetUsername);

    /**
     * Unfollows a target user on behalf of the current user.
     *
     * @param currentUsername the username of the acting user
     * @param targetUsername the username of the user to be unfollowed
     * @return a success message if the unfollow action is performed,
     *         or an error message if the user is not already following the target
     */
    BaseApiResponse<String> unfollowUser(String currentUsername, String targetUsername);

    /**
     * Checks whether the current user is following the target user.
     *
     * @param currentUsername the username of the user performing the check
     * @param targetUsername the username of the user being checked
     * @return {@code true} if the current user follows the target, {@code false} otherwise,
     *         wrapped in a successful {@link BaseApiResponse}
     */
    BaseApiResponse<Boolean> isFollowing(String currentUsername, String targetUsername);

}
