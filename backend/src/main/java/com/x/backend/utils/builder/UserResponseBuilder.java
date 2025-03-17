package com.x.backend.utils.builder;

import com.x.backend.dto.auth.response.UserResponse;
import com.x.backend.models.entities.ApplicationUser;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserResponseBuilder {

    public UserResponse buildUserResponse(ApplicationUser u) {
        return new UserResponse(
                u.getId(),
                u.getEmail(),
                u.getUsername(),
                u.getFirstName(),
                u.getLastName(),
                u.getPhone(),
                u.getDateOfBirth(),
                u.isVerifiedAccount(),
                u.isPrivateAccount(),
                u.getBio(),
                u.getNickname(),
                u.getLocation(),
                u.getWebsiteUrl(),
                u.getCreatedAt(),
                u.getFollowers().size(),
                u.getFollowing().size()
        );
    }

    public List<UserResponse> buildUserResponses(List<ApplicationUser> users) {
        List<UserResponse> userResponses = new ArrayList<>();
        for (ApplicationUser u : users) {
            userResponses.add(buildUserResponse(u));
        }
        return userResponses;
    }

}
