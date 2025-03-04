package com.x.backend.services.auth.strategies;

import com.x.backend.dto.LoginRequest;
import com.x.backend.models.entities.ApplicationUser;

public interface LoginStrategy {

    boolean supports(String loginType);
    ApplicationUser authenticate(LoginRequest req);

}
