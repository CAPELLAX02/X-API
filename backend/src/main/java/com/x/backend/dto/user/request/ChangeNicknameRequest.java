package com.x.backend.dto.user.request;

import jakarta.validation.constraints.NotBlank;

public record ChangeNicknameRequest(

        @NotBlank(message = "New nickname is required.")
        String newNickname

) {
}
