package com.x.backend.controllers;

import com.x.backend.models.ApplicationUser;
import com.x.backend.services.image.ImageService;
import com.x.backend.services.token.JwtService;
import com.x.backend.services.user.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;
    private final ImageService imageService;

    public UserController(UserService userService, JwtService jwtService, AuthenticationManager authenticationManager, ImageService imageService) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.imageService = imageService;
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

}
