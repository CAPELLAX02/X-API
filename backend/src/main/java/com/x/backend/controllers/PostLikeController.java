package com.x.backend.controllers;

import com.x.backend.models.entities.ApplicationUser;
import com.x.backend.services.post.like.PostLikeService;
import com.x.backend.utils.api.BaseApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
public class PostLikeController {

    private final PostLikeService postLikeService;

    public PostLikeController(PostLikeService postLikeService) {
        this.postLikeService = postLikeService;
    }

    @PutMapping("/{postId}/like")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<String>> likePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal ApplicationUser user
    ) {
        String username = user.getUsername();
        BaseApiResponse<String> res = postLikeService.likePost(username, postId);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @DeleteMapping("/{postId}/unlike")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<String>> unlikePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal ApplicationUser user
    ) {
        String username = user.getUsername();
        BaseApiResponse<String> res = postLikeService.unlikePost(username, postId);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @GetMapping("/{postId}/likes")
    public ResponseEntity<BaseApiResponse<Long>> getPostLikeCount(@PathVariable Long postId) {
        BaseApiResponse<Long> res = postLikeService.getLikeCount(postId);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

}
