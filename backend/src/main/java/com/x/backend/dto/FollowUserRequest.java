package com.x.backend.dto;

import jakarta.validation.constraints.NotBlank;

public record FollowUserRequest(

        @NotBlank(message = "Username of the followee cannot be blank")
        String followeeUsername

) {
}
