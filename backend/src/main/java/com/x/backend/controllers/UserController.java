package com.x.backend.controllers;

import com.x.backend.dto.auth.response.UserResponse;
import com.x.backend.models.entities.ApplicationUser;
import com.x.backend.services.user.UserService;
import com.x.backend.utils.api.BaseApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<BaseApiResponse<UserResponse>> getCurrentUser(
            @AuthenticationPrincipal ApplicationUser user
    ) {
        String username = user.getUsername();
        BaseApiResponse<UserResponse> res = userService.getCurrentUser(username);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

}
