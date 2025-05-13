package com.x.backend.controllers;

import com.x.backend.models.user.user.ApplicationUser;
import com.x.backend.services.like.CommentLikeService;
import com.x.backend.utils.api.BaseApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    public CommentLikeController(CommentLikeService commentLikeService) {
        this.commentLikeService = commentLikeService;
    }

    @PutMapping("/{commentId}/like")
    @PreAuthorize("hasRole('USER_ROLE')")
    public ResponseEntity<BaseApiResponse<String>> likeComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal ApplicationUser user
    ) {
        BaseApiResponse<String> res = commentLikeService.likeComment(user.getUsername(), commentId);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @DeleteMapping("/{commentId}/unlike")
    public ResponseEntity<BaseApiResponse<String>> unlikeComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal ApplicationUser user
    ) {
        BaseApiResponse<String> res = commentLikeService.unlikeComment(user.getUsername(), commentId);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @GetMapping("/{commentId}/like-count")
    public ResponseEntity<BaseApiResponse<Long>> countCommentLikes(@PathVariable Long commentId) {
        BaseApiResponse<Long> res = commentLikeService.countCommentLikes(commentId);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

}
