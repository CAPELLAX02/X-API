package com.x.backend.controllers;

import com.x.backend.dto.comment.request.CreateCommentRequest;
import com.x.backend.dto.comment.response.CommentResponse;
import com.x.backend.models.entities.ApplicationUser;
import com.x.backend.services.comment.CommentService;
import com.x.backend.utils.api.BaseApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // TODO: Add @Valid annotation every required endpoint requests...

    @PostMapping("/post/{postId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<CommentResponse>> createComment(
            @PathVariable Long postId,
            @Valid @RequestBody CreateCommentRequest req,
            @AuthenticationPrincipal ApplicationUser user
    ) {
        BaseApiResponse<CommentResponse> res = commentService.createComment(user.getUsername(), postId, req);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @PutMapping("/{commentId}/edit")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<CommentResponse>> editComment(
            @PathVariable Long commentId,
            @RequestBody @Valid String newContent,
            @AuthenticationPrincipal ApplicationUser user
    ) {
        BaseApiResponse<CommentResponse> res = commentService.editComment(user.getUsername(), commentId, newContent);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @DeleteMapping("/{commentId}/delete")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<String>> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal ApplicationUser user
    ) {
        BaseApiResponse<String> res = commentService.deleteComment(user.getUsername(), commentId);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<BaseApiResponse<List<CommentResponse>>> getCommentsByPost(@PathVariable Long postId) {
        BaseApiResponse<List<CommentResponse>> res = commentService.getCommentsByPost(postId);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

}
