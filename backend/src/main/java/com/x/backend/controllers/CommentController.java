package com.x.backend.controllers;

import com.x.backend.dto.comment.request.EditCommentRequest;
import com.x.backend.dto.comment.response.CommentResponse;
import com.x.backend.models.user.ApplicationUser;
import com.x.backend.services.comment.PostCommentService;
import com.x.backend.utils.api.BaseApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final PostCommentService postCommentService;

    public CommentController(final PostCommentService postCommentService) {
        this.postCommentService = postCommentService;
    }

    @PutMapping("/{commentId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<CommentResponse>> editComment(
            @PathVariable Long commentId,
            @Valid @RequestBody EditCommentRequest req,
            @AuthenticationPrincipal ApplicationUser user
    ) {
        BaseApiResponse<CommentResponse> res = postCommentService.editComment(user.getUsername(), commentId, req);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @DeleteMapping("/{commentId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<String>> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal ApplicationUser user
    ) {
        BaseApiResponse<String> res = postCommentService.deleteComment(user.getUsername(), commentId);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

}
