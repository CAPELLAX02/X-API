package com.x.backend.services.user;

import com.x.backend.dto.auth.response.UserResponse;
import com.x.backend.models.entities.ApplicationUser;
import com.x.backend.utils.api.BaseApiResponse;

public interface UserService {

    ApplicationUser getUserByUsername(String username);

    BaseApiResponse<UserResponse> getCurrentUser(String username);

}
