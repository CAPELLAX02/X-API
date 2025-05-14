package com.x.backend.services.auth.strategies;

import com.x.backend.dto.auth.request.LoginRequest;
import com.x.backend.models.user.ApplicationUser;

public interface LoginStrategy {

    boolean supports(String loginType);
    ApplicationUser authenticate(LoginRequest req);

}
