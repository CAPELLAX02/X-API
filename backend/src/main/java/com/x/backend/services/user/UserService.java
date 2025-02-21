package com.x.backend.services.user;

import com.x.backend.dto.*;
import com.x.backend.exceptions.EmailFailedToSentException;
import com.x.backend.models.ApplicationUser;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

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
    Set<ApplicationUser> followUser(String followerUsername, String followeeUsername);
    Set<ApplicationUser> getFollowings(String username);
    Set<ApplicationUser> getFollowers(String username);

}
