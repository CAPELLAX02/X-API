package com.x.backend.controllers;

import com.x.backend.models.user.ApplicationUser;
import com.x.backend.services.post.PostRepostService;
import com.x.backend.utils.api.BaseApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
public class PostRepostController {

    private final PostRepostService postRepostService;

    public PostRepostController(PostRepostService postRepostService) {
        this.postRepostService = postRepostService;
    }

    @PostMapping("/{postId}/repost")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<String>> repostPost(
            @PathVariable Long postId,
            @AuthenticationPrincipal ApplicationUser user
    ) {
        BaseApiResponse<String> res = postRepostService.repostPost(user.getUsername(), postId);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @DeleteMapping("/{postId}/repost/undo")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<String>> undoRepost(
            @PathVariable Long postId,
            @AuthenticationPrincipal ApplicationUser user
    ) {
        BaseApiResponse<String> res = postRepostService.undoRepost(user.getUsername(), postId);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @GetMapping("/{postId}/count-repost")
    public ResponseEntity<BaseApiResponse<Long>> getRepostCount(@PathVariable Long postId) {
        BaseApiResponse<Long> res = postRepostService.getRepostCount(postId);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @GetMapping("/{postId}/repost/status")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<Boolean>> hasUserReposted(
            @PathVariable Long postId,
            @AuthenticationPrincipal ApplicationUser user
    ) {
        BaseApiResponse<Boolean> res = postRepostService.hasUserReposted(user.getUsername(), postId);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

}
