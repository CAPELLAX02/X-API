package com.x.backend.controllers;

import com.x.backend.dto.FollowUserRequest;
import com.x.backend.models.ApplicationUser;
import com.x.backend.services.token.JwtService;
import com.x.backend.services.user.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @GetMapping("/verify")
    public ResponseEntity<ApplicationUser> verifyIdentity(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken
    ) {
        String username = jwtService.getUsernameFromToken(accessToken);
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @PostMapping("/upload/pp")
    public ResponseEntity<ApplicationUser> uploadProfilePicture(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken,
            @RequestParam("image") MultipartFile file
    ) {
        String username = jwtService.getUsernameFromToken(accessToken);
        return ResponseEntity.ok(userService.setProfileOrBanner(username, file, "profile_picture"));
    }

    @PostMapping("/upload/banner")
    public ResponseEntity<ApplicationUser> uploadBannerPicture(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken,
            @RequestParam("image") MultipartFile file
    ) {
        String username = jwtService.getUsernameFromToken(accessToken);
        return ResponseEntity.ok(userService.setProfileOrBanner(username, file, "banner_picture"));
    }

    @PutMapping("/follow")
    public ResponseEntity<Set<ApplicationUser>> followUser(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken,
            @RequestBody FollowUserRequest followUserRequest
    ) {
        String followerUser = jwtService.getUsernameFromToken(accessToken);
        String followeeUser = followUserRequest.followeeUsername();
        return ResponseEntity.ok(userService.followUser(followerUser, followeeUser));
    }

    @GetMapping("/followings/{username}")
    public ResponseEntity<Set<ApplicationUser>> getFollowings(
            @PathVariable String username
    ) {
        Set<ApplicationUser> followings = userService.getFollowings(username);
        return ResponseEntity.ok(followings);
    }

    @GetMapping("/followers/{username}")
    public ResponseEntity<Set<ApplicationUser>> getFollowers(
            @PathVariable String username
    ) {
        Set<ApplicationUser> followers = userService.getFollowers(username);
        return ResponseEntity.ok(followers);
    }

}
