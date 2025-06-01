package com.x.backend.controllers;

import com.x.backend.models.user.ApplicationUser;
import com.x.backend.services.user.block.UserBlockService;
import com.x.backend.utils.api.BaseApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserBlockController {

    private final UserBlockService userBlockService;

    public UserBlockController(UserBlockService userBlockService) {
        this.userBlockService = userBlockService;
    }

    @PostMapping("/{username}/block")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<String>> blockUser(
            @AuthenticationPrincipal ApplicationUser blockerUser,
            @PathVariable("username") String blockedUsername
    ) {
        BaseApiResponse<String> res = userBlockService.blockUser(blockerUser.getUsername(), blockedUsername);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @DeleteMapping("/{username}/unblock")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<String>> unblockUser(
            @AuthenticationPrincipal ApplicationUser blockerUser,
            @PathVariable("username") String blockedUsername
    ) {
        BaseApiResponse<String> res = userBlockService.unblockUser(blockerUser.getUsername(), blockedUsername);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @GetMapping("/{username}/is-blocked")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<Boolean>> isUserBlocked(
            @AuthenticationPrincipal ApplicationUser actorUser,
            @PathVariable("username") String targetUsername
    ) {
        BaseApiResponse<Boolean> res = userBlockService.isUserBlocked(actorUser.getUsername(), targetUsername);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @GetMapping("/blocked")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<List<String>>> getBlockedUsers(
            @AuthenticationPrincipal ApplicationUser currentUser
    ) {
        BaseApiResponse<List<String>> res = userBlockService.getBlockedUsers(currentUser.getUsername());
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @GetMapping("/blocked-me")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<List<String>>> getUsersBlockedMe(
            @AuthenticationPrincipal ApplicationUser currentUser
    ) {
        BaseApiResponse<List<String>> res = userBlockService.getUsersBlockingMe(currentUser.getUsername());
        return ResponseEntity.status(res.getStatus()).body(res);
    }

}
