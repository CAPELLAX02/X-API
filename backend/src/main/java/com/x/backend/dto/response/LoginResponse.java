package com.x.backend.dto.response;

import com.x.backend.models.ApplicationUser;

public record LoginResponse(
        ApplicationUser user,
        String accessToken
) {
    @Override
    public String toString() {
        return "LoginResponse{" +
                "user=" + user +
                ", accessToken='" + accessToken + '\'' +
                '}';
    }
}
