package com.x.backend.dto.user.request;

import jakarta.validation.constraints.NotBlank;

public record SetNicknameRequest(

        @NotBlank(message = "Nickname is required.")
        String nickname

) {
}
