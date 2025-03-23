package com.x.backend.controllers;

import com.x.backend.dto.user.response.PrivacySettingsResponse;
import com.x.backend.dto.user.response.UserResponse;
import com.x.backend.dto.user.request.*;
import com.x.backend.models.entities.ApplicationUser;
import com.x.backend.services.user.UserService;
import com.x.backend.services.user.follow.FollowService;
import com.x.backend.services.user.privacy.PrivacySettingsService;
import com.x.backend.utils.api.BaseApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Consumer;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final PrivacySettingsService privacySettingsService;
    private final FollowService followService;

    public UserController(UserService userService, PrivacySettingsService privacySettingsService, FollowService followService) {
        this.userService = userService;
        this.privacySettingsService = privacySettingsService;
        this.followService = followService;
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<UserResponse>> getCurrentUser(
            @AuthenticationPrincipal ApplicationUser user
    ) {
        String username = user.getUsername();
        BaseApiResponse<UserResponse> res = userService.getCurrentUser(username);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PutMapping("/me/nickname")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<String>> setNickname(
            @AuthenticationPrincipal ApplicationUser user,
            @RequestBody SetNicknameRequest req
    ) {
        String username = user.getUsername();
        BaseApiResponse<String> res = userService.setNickname(username, req);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PutMapping("/me/update/nickname")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<String>> changeNickname(
            @AuthenticationPrincipal ApplicationUser user,
            @RequestBody ChangeNicknameRequest req
    ) {
        String username = user.getUsername();
        BaseApiResponse<String> res = userService.changeNickname(username, req);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @GetMapping("/nickname/{nickname}")
    public ResponseEntity<BaseApiResponse<UserResponse>> getUserByNickname(
            @PathVariable String nickname
    ) {
        BaseApiResponse<UserResponse> res = userService.getUserByNickname(nickname);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @GetMapping("/nickname")
    public ResponseEntity<BaseApiResponse<List<UserResponse>>> getAllUsersByNickname(
            @RequestParam("nickname") String nickname
    ) {
        BaseApiResponse<List<UserResponse>> res = userService.getAllUsersByNickname(nickname);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PutMapping("/me/profile/bio")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<UserResponse>> updateBio(
            @AuthenticationPrincipal ApplicationUser user,
            @RequestBody UpdateBioRequest req
    ) {
        String username = user.getUsername();
        Consumer<ApplicationUser> bioUpdater = u -> u.setBio(req.bio());
        BaseApiResponse<UserResponse> res = userService.updateUser(username, bioUpdater);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PutMapping("/me/profile/location")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<UserResponse>> updateLocation(
            @AuthenticationPrincipal ApplicationUser user,
            @RequestBody UpdateLocationRequest req
    ) {
        String username = user.getUsername();
        Consumer<ApplicationUser> locationUpdater = u -> u.setLocation(req.location());
        BaseApiResponse<UserResponse> res = userService.updateUser(username, locationUpdater);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PutMapping("/me/profile/website")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<UserResponse>> updateWebsiteUrl(
            @AuthenticationPrincipal ApplicationUser user,
            @RequestBody UpdateWebsiteRequest req
    ) {
        String username = user.getUsername();
        Consumer<ApplicationUser> websiteUpdater = u -> u.setWebsiteUrl(req.websiteUrl());
        BaseApiResponse<UserResponse> res = userService.updateUser(username, websiteUpdater);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @GetMapping("/me/privacy-settings")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<PrivacySettingsResponse>> getPrivacySettingsForCurrentUser(
            @AuthenticationPrincipal ApplicationUser user
    ) {
        String username = user.getUsername();
        BaseApiResponse<PrivacySettingsResponse> res = privacySettingsService.getPrivacySettingsForCurrentUser(username);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PutMapping("/me/update/privacy-settings")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<PrivacySettingsResponse>> updatePrivacySettingsForCurrentUser(
            @AuthenticationPrincipal ApplicationUser user,
            @RequestBody UpdatePrivacySettingsRequest req
    ) {
        String username = user.getUsername();
        BaseApiResponse<PrivacySettingsResponse> res = privacySettingsService.updatePrivacySettingsForCurrentUser(username, req);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/me/followers")
    public ResponseEntity<BaseApiResponse<List<UserResponse>>> getAllFollowers(
            @AuthenticationPrincipal ApplicationUser user
    ) {
        String username = user.getUsername();
        BaseApiResponse<List<UserResponse>> res = followService.getAllFollowers(username);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/me/following")
    public ResponseEntity<BaseApiResponse<List<UserResponse>>> getAllFollowing(
            @AuthenticationPrincipal ApplicationUser user
    ) {
        String username = user.getUsername();
        BaseApiResponse<List<UserResponse>> res = followService.getAllFollowings(username);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/me/follow/{username}")
    public ResponseEntity<BaseApiResponse<String>> followUser(
            @AuthenticationPrincipal ApplicationUser user,
            @PathVariable("username") String targetUsername
    ) {
        String username = user.getUsername();
        BaseApiResponse<String> res = followService.followUser(username, targetUsername);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/me/unfollow/{username}")
    public ResponseEntity<BaseApiResponse<String>> unfollowUser(
            @AuthenticationPrincipal ApplicationUser user,
            @PathVariable("username") String targetUsername
    ) {
        String username = user.getUsername();
        BaseApiResponse<String> res = followService.unfollowUser(username, targetUsername);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/me/check/{username}")
    public ResponseEntity<BaseApiResponse<Boolean>> isFollowing(
            @AuthenticationPrincipal ApplicationUser user,
            @PathVariable("username") String targetUsername
    ) {
        String username = user.getUsername();
        BaseApiResponse<Boolean> res = followService.isFollowing(username, targetUsername);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

}
