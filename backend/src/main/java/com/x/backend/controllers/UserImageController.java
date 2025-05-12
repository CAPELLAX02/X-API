package com.x.backend.controllers;

import com.x.backend.dto.image.response.ImageResponse;
import com.x.backend.models.entities.ApplicationUser;
import com.x.backend.services.image.UserImageServiceImpl;
import com.x.backend.utils.api.BaseApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users/image")
public class UserImageController {

    private final UserImageServiceImpl userImageService;

    public UserImageController(UserImageServiceImpl userImageService) {
        this.userImageService = userImageService;
    }

    @PostMapping("/upload/pp")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<ImageResponse>> uploadProfileImage(
            @RequestParam("image") MultipartFile file,
            @AuthenticationPrincipal ApplicationUser user
    ) {
        BaseApiResponse<ImageResponse> res = userImageService.uploadProfileImage(file, user.getUsername());
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PostMapping("/upload/banner")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<ImageResponse>> uploadBannerImage(
            @RequestParam("image") MultipartFile file,
            @AuthenticationPrincipal ApplicationUser user
    ) {
        BaseApiResponse<ImageResponse> res = userImageService.uploadBannerImage(file, user.getUsername());
        return ResponseEntity.status(res.getStatus()).body(res);
    }

}
