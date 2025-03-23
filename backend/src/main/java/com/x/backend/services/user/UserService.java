package com.x.backend.services.user;

import com.x.backend.dto.user.response.UserResponse;
import com.x.backend.dto.user.request.ChangeNicknameRequest;
import com.x.backend.dto.user.request.SetNicknameRequest;
import com.x.backend.models.entities.ApplicationUser;
import com.x.backend.utils.api.BaseApiResponse;

import java.util.List;
import java.util.function.Consumer;

public interface UserService {

    ApplicationUser getUserByUsername(String username);

    BaseApiResponse<UserResponse> getCurrentUser(String username);
    BaseApiResponse<String> setNickname(String username, SetNicknameRequest req);
    BaseApiResponse<String> changeNickname(String username, ChangeNicknameRequest req);
    BaseApiResponse<UserResponse> getUserByNickname(String nickname);
    BaseApiResponse<List<UserResponse>> getAllUsersByNickname(String nickname);

    BaseApiResponse<UserResponse> updateUser(String username, Consumer<ApplicationUser> updater);

}
