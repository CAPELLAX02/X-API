package com.x.backend.services.user;

import com.x.backend.dto.authentication.request.*;
import com.x.backend.dto.authentication.response.LoginResponse;
import com.x.backend.exceptions.EmailFailedToSentException;
import com.x.backend.models.ApplicationUser;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    ApplicationUser getUserByUsername(String username);
    ApplicationUser registerUser(RegisterUserRequest registerUserRequest);
    String startEmailVerification(StartEmailVerificationRequest startEmailVerificationRequest) throws EmailFailedToSentException;
    ApplicationUser completeEmailVerification(CompleteEmailVerificationRequest completeEmailVerificationRequest) throws EmailFailedToSentException;
    ApplicationUser updateUserPassword(UpdatePasswordRequest updatePasswordRequest);
    ApplicationUser updateUser(ApplicationUser user);
    ApplicationUser updateUserPhoneNumber(UpdatePhoneNumberRequest updatePhoneNumberRequest);
    ApplicationUser setProfileOrBanner(String username, MultipartFile image, String prefix);
    LoginResponse loginUser(LoginRequest loginRequest);

}
