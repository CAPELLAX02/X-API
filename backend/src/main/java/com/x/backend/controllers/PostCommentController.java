package com.x.backend.controllers;

import com.x.backend.dto.comment.request.CreateCommentRequest;
import com.x.backend.dto.comment.request.EditCommentRequest;
import com.x.backend.dto.comment.response.CommentResponse;
import com.x.backend.models.user.user.ApplicationUser;
import com.x.backend.utils.api.BaseApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostCommentController {

    private final PostCommentService postCommentService;

    public PostCommentController(PostCommentService postCommentService) {
        this.postCommentService = postCommentService;
    }

    @PostMapping("/{postId}/comments")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<CommentResponse>> createComment(
            @PathVariable Long postId,
            @Valid @RequestBody CreateCommentRequest req,
            @AuthenticationPrincipal ApplicationUser user
    ) {
        BaseApiResponse<CommentResponse> res = postCommentService.createComment(user.getUsername(), postId, req);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PutMapping("/comments/{commentId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<CommentResponse>> editComment(
            @PathVariable Long commentId,
            @Valid @RequestBody EditCommentRequest req,
            @AuthenticationPrincipal ApplicationUser user
    ) {
        BaseApiResponse<CommentResponse> res = postCommentService.editComment(user.getUsername(), commentId, req);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @DeleteMapping("/comments/{commentId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<String>> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal ApplicationUser user
    ) {
        BaseApiResponse<String> res = postCommentService.deleteComment(user.getUsername(), commentId);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<BaseApiResponse<List<CommentResponse>>> getCommentsByPost(@PathVariable Long postId) {
        BaseApiResponse<List<CommentResponse>> res = postCommentService.getCommentsByPost(postId);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

}
