package com.x.backend.controllers;

import com.x.backend.dto.comment.request.CreateCommentRequest;
import com.x.backend.dto.comment.response.CommentResponse;
import com.x.backend.models.user.ApplicationUser;
import com.x.backend.services.comment.PostCommentService;
import com.x.backend.utils.api.BaseApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts/{postId}/comments")
public class PostCommentController {

    private final PostCommentService postCommentService;

    public PostCommentController(final PostCommentService postCommentService) {
        this.postCommentService = postCommentService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseApiResponse<CommentResponse>> createComment(
            @PathVariable Long postId,
            @Valid @RequestBody CreateCommentRequest req,
            @AuthenticationPrincipal ApplicationUser user
    ) {
        BaseApiResponse<CommentResponse> res = postCommentService.createComment(user.getUsername(), postId, req);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

    @GetMapping
    public ResponseEntity<BaseApiResponse<List<CommentResponse>>> getCommentsByPost(@PathVariable Long postId) {
        BaseApiResponse<List<CommentResponse>> res = postCommentService.getCommentsByPost(postId);
        return ResponseEntity.status(res.getStatus()).body(res);
    }

}
