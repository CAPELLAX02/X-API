package com.x.backend.controllers;

import com.x.backend.dto.user.request.*;
import com.x.backend.dto.user.response.PrivacySettingsResponse;
import com.x.backend.dto.user.response.UserResponse;
import com.x.backend.models.user.user.ApplicationUser;
import com.x.backend.services.user.UserService;
import com.x.backend.services.user.follow.FollowService;
import com.x.backend.services.user.privacy.PrivacySettingsService;
import com.x.backend.utils.api.BaseApiResponse;
import jakarta.validation.Valid;
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

    public UserController(UserService userService,
                          PrivacySettingsService privacySettingsService,
                          FollowService followService
    ) {
        this.userService = userService;
        this.privacySettingsService = privacySettingsService;
        this.followService = followService;
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<UserResponse>> getCurrentUser(
            @AuthenticationPrincipal ApplicationUser user
    ) {
        BaseApiResponse<UserResponse> res = userService.getCurrentUser(user.getUsername());
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PutMapping("/me/nickname")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<String>> setNickname(
            @AuthenticationPrincipal ApplicationUser user,
            @Valid @RequestBody SetNicknameRequest req
    ) {
        BaseApiResponse<String> res = userService.setNickname(user.getUsername(), req);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PutMapping("/me/update/nickname")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<String>> changeNickname(
            @AuthenticationPrincipal ApplicationUser user,
            @Valid @RequestBody ChangeNicknameRequest req
    ) {
        BaseApiResponse<String> res = userService.changeNickname(user.getUsername(), req);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @GetMapping("/nickname/{nickname}")
    public ResponseEntity<BaseApiResponse<UserResponse>> getUserByNickname(@PathVariable String nickname) {
        BaseApiResponse<UserResponse> res = userService.getUserByNickname(nickname);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @GetMapping("/search")
    public ResponseEntity<BaseApiResponse<List<UserResponse>>> searchUsersByNickname(@RequestParam("nickname") String nickname) {
        BaseApiResponse<List<UserResponse>> res = userService.getAllUsersByNickname(nickname);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PutMapping("/me/profile/bio")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<UserResponse>> updateBio(
            @AuthenticationPrincipal ApplicationUser user,
            @Valid @RequestBody UpdateBioRequest req
    ) {
        Consumer<ApplicationUser> bioUpdater = u -> u.setBio(req.bio());
        BaseApiResponse<UserResponse> res = userService.updateUser(user.getUsername(), bioUpdater);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PutMapping("/me/profile/location")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<UserResponse>> updateLocation(
            @AuthenticationPrincipal ApplicationUser user,
            @Valid @RequestBody UpdateLocationRequest req
    ) {
        Consumer<ApplicationUser> locationUpdater = u -> u.setLocation(req.location());
        BaseApiResponse<UserResponse> res = userService.updateUser(user.getUsername(), locationUpdater);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PutMapping("/me/profile/website")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<UserResponse>> updateWebsiteUrl(
            @AuthenticationPrincipal ApplicationUser user,
            @Valid @RequestBody UpdateWebsiteRequest req
    ) {
        Consumer<ApplicationUser> websiteUpdater = u -> u.setWebsiteUrl(req.websiteUrl());
        BaseApiResponse<UserResponse> res = userService.updateUser(user.getUsername(), websiteUpdater);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @GetMapping("/me/privacy-settings")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<PrivacySettingsResponse>> getPrivacySettingsForCurrentUser(
            @AuthenticationPrincipal ApplicationUser user
    ) {
        BaseApiResponse<PrivacySettingsResponse> res = privacySettingsService.getPrivacySettingsForCurrentUser(user.getUsername());
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PutMapping("/me/update/privacy-settings")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<PrivacySettingsResponse>> updatePrivacySettingsForCurrentUser(
            @AuthenticationPrincipal ApplicationUser user,
            @Valid @RequestBody UpdatePrivacySettingsRequest req
    ) {
        BaseApiResponse<PrivacySettingsResponse> res = privacySettingsService.updatePrivacySettingsForCurrentUser(user.getUsername(), req);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/me/followers")
    public ResponseEntity<BaseApiResponse<List<UserResponse>>> getAllFollowers(
            @AuthenticationPrincipal ApplicationUser user
    ) {
        BaseApiResponse<List<UserResponse>> res = followService.getAllFollowers(user.getUsername());
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/me/following")
    public ResponseEntity<BaseApiResponse<List<UserResponse>>> getAllFollowing(
            @AuthenticationPrincipal ApplicationUser user
    ) {
        BaseApiResponse<List<UserResponse>> res = followService.getAllFollowings(user.getUsername());
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/me/follow/{username}")
    public ResponseEntity<BaseApiResponse<String>> followUser(
            @AuthenticationPrincipal ApplicationUser user,
            @PathVariable("username") String targetUsername
    ) {
        BaseApiResponse<String> res = followService.followUser(user.getUsername(), targetUsername);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/me/unfollow/{username}")
    public ResponseEntity<BaseApiResponse<String>> unfollowUser(
            @AuthenticationPrincipal ApplicationUser user,
            @PathVariable("username") String targetUsername
    ) {
        BaseApiResponse<String> res = followService.unfollowUser(user.getUsername(), targetUsername);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/me/check/{username}")
    public ResponseEntity<BaseApiResponse<Boolean>> isFollowing(
            @AuthenticationPrincipal ApplicationUser user,
            @PathVariable("username") String targetUsername
    ) {
        BaseApiResponse<Boolean> res = followService.isFollowing(user.getUsername(), targetUsername);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

}
