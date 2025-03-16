package com.x.backend.dto.auth.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String email,
        String username,
        String firstName,
        String lastName,
        String phone,
        LocalDate dateOfBirth,
        boolean isVerifiedAccount,
        boolean isPrivateAccount,
        String bio,
        String nickname,
        String location,
        String websiteUrl,
        boolean displayDateOfBirth,
        LocalDateTime createdAt,
        int followerCount,
        int followingCount
) {
}
