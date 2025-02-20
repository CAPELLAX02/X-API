package com.x.backend.services.user;

import com.x.backend.dto.authentication.request.RegisterUserRequest;
import com.x.backend.exceptions.EmailFailedToSentException;
import com.x.backend.models.ApplicationUser;

public interface UserService {

    ApplicationUser getUserByUsername(String username);
    ApplicationUser registerUser(RegisterUserRequest registerUserRequest);
    void generateEmailVerification(String username) throws EmailFailedToSentException;
    ApplicationUser verifyEmail(String username, Long verificationCode);
    ApplicationUser setPassword(String username, String password);
    ApplicationUser updateUser(ApplicationUser user);

}
