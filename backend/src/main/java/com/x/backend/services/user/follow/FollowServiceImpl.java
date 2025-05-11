package com.x.backend.services.user.follow;

import com.x.backend.dto.user.response.UserResponse;
import com.x.backend.models.entities.ApplicationUser;
import com.x.backend.repositories.ApplicationUserRepository;
import com.x.backend.services.user.UserService;
import com.x.backend.utils.api.BaseApiResponse;
import com.x.backend.utils.builder.UserResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class FollowServiceImpl implements FollowService {

    private final UserService userService;
    private final ApplicationUserRepository applicationUserRepository;
    private final UserResponseBuilder userResponseBuilder;

    public FollowServiceImpl(UserService userService,
                             ApplicationUserRepository applicationUserRepository,
                             UserResponseBuilder userResponseBuilder
    ) {
        this.userService = userService;
        this.applicationUserRepository = applicationUserRepository;
        this.userResponseBuilder = userResponseBuilder;
    }

    @Override
    public BaseApiResponse<List<UserResponse>> getAllFollowers(String currentUsername) {
        ApplicationUser user = userService.getUserByUsername(currentUsername);
        List<ApplicationUser> followers = new ArrayList<>(user.getFollowers());
        List<UserResponse> followerResponses = userResponseBuilder.buildUserResponses(followers);
        return BaseApiResponse.success(followerResponses, "Followers retrieved successfully.");
    }

    @Override
    public BaseApiResponse<List<UserResponse>> getAllFollowings(String currentUsername) {
        ApplicationUser user = userService.getUserByUsername(currentUsername);
        List<ApplicationUser> followings = new ArrayList<>(user.getFollowing());
        List<UserResponse> followingResponses = userResponseBuilder.buildUserResponses(followings);
        return BaseApiResponse.success(followingResponses, "Followings retrieved successfully.");
    }

    @Override
    public BaseApiResponse<String> followUser(String currentUsername, String targetUsername) {
        if (currentUsername.equals(targetUsername)) {
            return BaseApiResponse.error("You cannot follow yourself.", HttpStatus.BAD_REQUEST);
        }

        ApplicationUser currentUser = userService.getUserByUsername(currentUsername);
        ApplicationUser targetUser = userService.getUserByUsername(targetUsername);

        if (currentUser.getFollowing().contains(targetUser)) {
            return BaseApiResponse.error("You are already following '" + targetUser.getNickname() + "'.", HttpStatus.BAD_REQUEST);
        }

        currentUser.getFollowing().add(targetUser);
        applicationUserRepository.save(currentUser);
        return BaseApiResponse.success("You are now following '" + targetUser.getNickname() + "'.");
    }

    @Override
    public BaseApiResponse<String> unfollowUser(String username, String targetUsername) {
        ApplicationUser currentUser = userService.getUserByUsername(username);
        ApplicationUser targetUser = userService.getUserByUsername(targetUsername);

        if (!currentUser.getFollowing().contains(targetUser)) {
            return BaseApiResponse.error("You are not following '" + targetUser.getNickname() + "' anyway.", HttpStatus.BAD_REQUEST);
        }

        currentUser.getFollowing().remove(targetUser);
        applicationUserRepository.save(currentUser);
        return BaseApiResponse.success("Unfollowed '" + targetUsername + "'.");
    }

    @Override
    public BaseApiResponse<Boolean> isFollowing(String currentUsername, String targetUsername) {
        ApplicationUser currentUser = userService.getUserByUsername(currentUsername);
        ApplicationUser targetUser = userService.getUserByUsername(targetUsername);
        Boolean isFollowing = currentUser.getFollowing().contains(targetUser);
        String responseMessage = isFollowing
                ? "You are following '" + targetUser.getNickname() + "'"
                : "You are not following '" + targetUser.getNickname() + "'.";
        return BaseApiResponse.success(isFollowing, responseMessage);
    }

}
